#include "../head.h"

void sig_child(int signo) {
	pid_t pid;
	int stat;
	while((pid = waitpid(-1, &stat,WNOHANG)) > 0)
		printf("child %d terminated\n", pid);
	return;
}

int main(void) {
	int lfd, cfd, i, opt;
	struct sockaddr_in serv_addr, clie_addr;
	struct sockaddr_in listen_addr, peer_addr;
	char listen_ip[BUFSIZ], peer_ip[BUFSIZ];
	char buf[BUFSIZ];
	time_t t;
	socklen_t clie_len, listen_len, peer_len;
	ssize_t n;

	if((lfd = socket(AF_INET, SOCK_STREAM, 0)) == -1)
		p_error("socket error");

	memset(&serv_addr, 0, sizeof(serv_addr));
	serv_addr.sin_family = AF_INET;
	serv_addr.sin_port = htons(SERV_PORT);
	serv_addr.sin_addr.s_addr = htonl(INADDR_ANY); 

	// 避免bind出现地址被使用问题
	opt = 1;
	setsockopt(lfd, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt));

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

	while(1) {
		clie_len = sizeof(clie_addr);

		if ((cfd = accept(lfd, (struct sockaddr *)&clie_addr, &clie_len))== -1)
			p_error("accept error");

		if((i = fork()) < 0)
			p_error("fork error");
		else if ( i == 0) {
			close(lfd);

			peer_len = sizeof(peer_addr);
			getpeername(cfd, (struct sockaddr *)&peer_addr, &peer_len);
			printf("%s:%d login now!\n\n",
					inet_ntop(AF_INET, &peer_addr.sin_addr.s_addr,
						peer_ip, sizeof(peer_ip)),
					ntohs(peer_addr.sin_port));

			while (1) {
				if ((n = recv(cfd, &buf, sizeof(buf), 0)) == 0)
					break;
				time(&t);
				printf("%s", ctime(&t));
				printf("[%s:%d]: %s\n", peer_ip, ntohs(peer_addr.sin_port), buf);
				if (strcmp("exit\n", buf) == 0) 
					break;
			
				for (i=0; i<n; i++)
					buf[i] = toupper(buf[i]);
				send(cfd, &buf, strlen(buf), 0);

				memset(&buf, 0, sizeof(buf));
			}

			printf("[%s:%d] exit now!\n", peer_ip, ntohs(peer_addr.sin_port));
			close(cfd);
			exit(0);
		} 
		signal(SIGCHLD, sig_child);
	}

	return 0;
}
