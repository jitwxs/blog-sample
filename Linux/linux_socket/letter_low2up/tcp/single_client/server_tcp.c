#include "../head.h"

int main(void) {

	int lfd, cfd, i;
	char buf[BUFSIZ], clie_ip[BUFSIZ];
	struct sockaddr_in serv_addr, clie_addr;
	socklen_t clie_addr_len;
	ssize_t n;

	/* 1.socket() */
	if ((lfd = socket(AF_INET, SOCK_STREAM, 0)) == -1)
		perror("socket error..");

	/* 2.create struct sockaddr_in */	
	memset(&serv_addr, 0, sizeof(serv_addr));
	serv_addr.sin_family = AF_INET;
	serv_addr.sin_port = htons(SERV_PORT);
	serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);

	/* 3.bind() */
	if(bind(lfd, (struct sockaddr *)&serv_addr, sizeof(serv_addr)) == -1)
		perror("bind error..");

	/* 4.listen() */
	if (listen(lfd, BACKLOG) == -1)
		perror("listen error..");

	/* 5.accept() */
	clie_addr_len = sizeof(clie_addr);
	cfd = accept(lfd, (struct sockaddr *)&clie_addr, &clie_addr_len);
	printf("client IP: %s, client port: %d\n",
			inet_ntop(AF_INET, &clie_addr.sin_addr.s_addr,
				clie_ip, sizeof(clie_ip)),
			ntohs(clie_addr.sin_port));

	/* 6.recv() and send() */
	while(1) {
		if ((n = recv(cfd, buf, sizeof(buf), 0)) == 0) {
			printf("client exit..\n");
			break;
		}
		printf("Receive msg: %s",buf);
	
		if(strcmp("exit\n", buf) == 0)
			break;

		for(i = 0; i < n; i++)
			buf[i] = toupper(buf[i]);
		
		printf("Send msg: %s",buf);
		send(cfd, buf, strlen(buf), 0);
		
		memset(buf, 0, sizeof(buf));
	}

	/* 6.close() */
	close(lfd);
	close(cfd);
}
