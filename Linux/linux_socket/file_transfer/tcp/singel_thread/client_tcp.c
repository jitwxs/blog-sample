#include "../head.h"

int main(int argc, char *argv[]) {
	FILE *fp;
	int cfd;
	struct sockaddr_in serv_addr;
	struct sockaddr_in connect_addr, peer_addr;
	struct msg *send_msg, *rec_msg;
	char connect_ip[BUFSIZ], peer_ip[BUFSIZ];
	char buf[BUFSIZ], source_filename[BUFSIZ], target_filename[BUFSIZ];
	ssize_t n;
	time_t start_time, end_time;
	socklen_t connect_len, peer_len;

	if((cfd = socket(AF_INET, SOCK_STREAM, 0)) == -1)
		p_error("socket error");

	send_msg = (struct msg*)malloc(MSG_SIZE);
	rec_msg = (struct msg*)malloc(MSG_SIZE);

	memset(&serv_addr, 0, sizeof(serv_addr));
	serv_addr.sin_family = AF_INET;
	serv_addr.sin_port = htons(SERV_PORT);
	inet_pton(AF_INET, argv[1], &serv_addr.sin_addr.s_addr);

	if ((connect(cfd, (struct sockaddr *)&serv_addr, sizeof(serv_addr))) == -1) {
		if(errno == ECONNREFUSED)
			p_error("connection refuse");
		else if(errno == EHOSTUNREACH)
			p_error("no route to host");
		else if(errno == ETIMEDOUT)
			p_error("connection time out");
	} else
		printf("Connect server succes!\n\n");

	connect_len = sizeof(connect_addr);
	getsockname(cfd, (struct sockaddr *)&connect_addr, &connect_len);
	printf("connect server address = %s:%d\n",
			inet_ntop(AF_INET, &connect_addr.sin_addr.s_addr,
				connect_ip, sizeof(connect_ip)),
			ntohs(connect_addr.sin_port));

	peer_len = sizeof(peer_addr);
	getpeername(cfd, (struct sockaddr *)&peer_addr, &peer_len);
	printf("connect peer address = %s:%d\n",
			inet_ntop(AF_INET, &peer_addr.sin_addr.s_addr,
				peer_ip, sizeof(peer_ip)),
			ntohs(peer_addr.sin_port));

	printf("================\n");

	while (1) {
		printf("please input source file name: ");
		scanf("%s", source_filename);
		
		if (!(fp = fopen(source_filename, "r"))) {
			perror("fopen");
			exit(1);
		}

		// send file name
		memset(send_msg, 0, sizeof(struct msg));
		printf("please input target file name: ");
		scanf("%s", target_filename);

		send_msg->type = MSG_FILENAME;
		send_msg->len = strlen(target_filename);
		memcpy(send_msg->data, &target_filename, send_msg->len);

		send(cfd, (void *)send_msg, sizeof(struct msg) + send_msg->len, 0);
		recv(cfd, (void *)rec_msg, MSG_SIZE, 0);
		if (rec_msg->type == MSG_ERROR) {
			printf("[ERROR] ");
			puts(rec_msg->data);
			exit(1);
		}

		// send file content
		time(&start_time);
		memset(send_msg, 0, sizeof(struct msg));
		while ((n = fread(&buf, sizeof(char), sizeof(buf), fp))) {
			send_msg->type = MSG_CONTENT;
			send_msg->len = n;
			memcpy(send_msg->data, &buf, send_msg->len);
			send(cfd, (void*)send_msg, sizeof(struct msg) + send_msg->len, 0);

			recv(cfd, (void *)rec_msg, MSG_SIZE, 0);
			if (rec_msg->type == MSG_ERROR) {
				printf("[ERROR] ");
				puts(rec_msg->data);
				exit(1);
			}
		}

		//compile send file 
		memset(send_msg, 0, sizeof(struct msg));
		if (n > 0) {
			send_msg->type = MSG_ERROR;
			strcpy(buf, "send file content error");
			send_msg->len = strlen(buf);
			memcpy(send_msg->data, &buf, send_msg->len);

			send(cfd, (void*)send_msg, sizeof(struct msg) + send_msg->len, 0);
		} else {
			time(&end_time);
			printf("[INFO] complete send file, consume %lfs.\n", difftime(end_time, start_time));
			if (fp) {
				fclose(fp);
				fp = NULL;	
			}
			send_msg->type = MSG_DONE;
			send_msg->len = 0;
			send(cfd, (void*)send_msg, sizeof(struct msg), 0);
			recv(cfd, (void *)rec_msg, MSG_SIZE, 0);
		}

loop:	printf(">(input 'q' to quit, input 'c' to continue) ");
		scanf("%s", buf);
		if (strcmp("c", buf) == 0)
			continue;
		else if (strcmp("q", buf) == 0) {
			//send exit
			memset(send_msg, 0, sizeof(struct msg));
			send_msg->type = MSG_EXIT;
			send_msg->len = 0;

			send(cfd, (void*)send_msg, sizeof(struct msg), 0);
			recv(cfd, (void *)rec_msg, MSG_SIZE, 0);

			if (rec_msg->type == MSG_ERROR) {
				printf("[ERROR] ");
				puts(rec_msg->data);
				exit(1);
			} else if (rec_msg->type == MSG_EXIT) {
				printf("[INFO] you will exit..\n");
				close(cfd);
				break;
			}
		} else 
			goto loop;
	}
	return 0;
}
