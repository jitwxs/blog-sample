#include "../head.h"

void sig_child(int signo) {
	pid_t pid;
	int stat;
	while((pid = waitpid(-1, &stat,WNOHANG)) > 0)
		printf("child %d terminated\n", pid);
	return;
}

int main(void) {
	int lfd, cfd, i;
	struct sockaddr_in serv_addr, clie_addr;
	struct sockaddr_in listen_addr, peer_addr;
	struct msg *send_msg, *rec_msg;
	char listen_ip[BUFSIZ], peer_ip[BUFSIZ];
	char buf[BUFSIZ], target_filename[BUFSIZ];
	time_t start_time, end_time;
	FILE *fp;
	socklen_t clie_len, listen_len, peer_len;

	if((lfd = socket(AF_INET, SOCK_STREAM, 0)) == -1)
		p_error("socket error");

	send_msg = (struct msg*)malloc(MSG_SIZE);
	rec_msg = (struct msg*)malloc(MSG_SIZE);

	memset(&serv_addr, 0, sizeof(serv_addr));
	serv_addr.sin_family = AF_INET;
	serv_addr.sin_port = htons(SERV_PORT);
	serv_addr.sin_addr.s_addr = htonl(INADDR_ANY); 

	if (bind(lfd, (struct sockaddr *)&serv_addr, sizeof(serv_addr)) == -1)
		p_error("bind error");

	if (listen(lfd, BACKLOG) == -1)
		p_error("listen error");

	listen_len = sizeof(listen_addr);
	getsockname(lfd, (struct sockaddr *)&listen_addr, &listen_len);
	printf("server listen address = %s:%d\n",
			inet_ntop(AF_INET, &listen_addr.sin_addr.s_addr,
				listen_ip, sizeof(listen_ip)),
			ntohs(listen_addr.sin_port));

	printf("Server service start success!\n\n");

	while (1) {
		clie_len = sizeof(clie_addr);

		if ((cfd = accept(lfd, (struct sockaddr *)&clie_addr, &clie_len)) == -1)
			p_error("accept error");

		if((i = fork()) < 0)
			perror("fork error");
		else if ( i == 0) {
			close(lfd);

			peer_len = sizeof(peer_addr);
			getpeername(cfd, (struct sockaddr *)&peer_addr, &peer_len);
			printf("%s:%d login now!\n\n",
					inet_ntop(AF_INET, &peer_addr.sin_addr.s_addr,
						peer_ip, sizeof(peer_ip)),
					ntohs(peer_addr.sin_port));

			while (1){
				memset(send_msg, 0, sizeof(struct msg));
				recv(cfd, (void*)rec_msg, MSG_SIZE, 0);

				switch (rec_msg->type) {
					case MSG_FILENAME:
						memset(&target_filename, 0, sizeof(target_filename));
						memcpy(&target_filename, rec_msg->data, rec_msg->len);
						fp = fopen(target_filename, "w+");
						//open file error
						if (!fp) {
							perror("fopen error");
							send_msg->type = MSG_ERROR;
							strcpy(buf, "open file error");
							send_msg->len = strlen(buf);
							memcpy(send_msg->data, &buf, send_msg->len);
							send(cfd, (void*)send_msg, sizeof(struct msg) + send_msg->len, 0);
						} else {
							time(&start_time);
							send_msg->type = MSG_ACK;
							send_msg->len = 0;
							send(cfd, (void*)send_msg, sizeof(struct msg), 0);
						}
						break;
					case MSG_CONTENT:
						if (!fp) {
							send_msg->type = MSG_ERROR;
							strcpy(buf, "file not open yet");
							send_msg->len = strlen(buf);
							memcpy(send_msg->data, &buf, send_msg->len);
							send(cfd, (void*)send_msg, sizeof(struct msg) + send_msg->len, 0);
						} else {
							fwrite(rec_msg->data, sizeof(char), rec_msg->len, fp);
							fflush(fp);

							send_msg->type = MSG_ACK;
							send_msg->len = 0;
							send(cfd, (void*)send_msg, sizeof(struct msg), 0);
						}
						break;
					case MSG_DONE:
						time(&end_time);
						printf("[INFO] %s:%d complete file send\n", peer_ip, ntohs(peer_addr.sin_port));	
						printf("[INFO] Save in : %s\n",target_filename);
						printf("[INFO] Consume : %lfs\n", difftime(end_time, start_time));

						//avoid double fcolse()
						if( fp) {
							fclose(fp);	
							fp = NULL;	
						}
						send_msg->type = MSG_ACK;
						send_msg->len = 0;
						send(cfd, (void*)send_msg, sizeof(struct msg), 0);
						break;
					case MSG_EXIT:
						printf("[INFO] client will exit\n");	

						send_msg->type = MSG_EXIT;
						send_msg->len = 0;
						send(cfd, (void*)send_msg, sizeof(struct msg), 0);
						break;
					default:
						break;
				}
			}
			close(cfd);
			exit(0);
		} 
		signal(SIGCHLD, sig_child);
	}
	return 0;
}
