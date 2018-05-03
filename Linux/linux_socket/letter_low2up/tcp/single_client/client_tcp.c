#include "../head.h"

int main(int argc, char *argv[]) {
	int cfd;
	struct sockaddr_in serv_addr;
	char buf[BUFSIZ];

	/* 1.socket() */
	if ((cfd = socket(AF_INET, SOCK_STREAM, 0)) == -1)
		perror("socket error..");

	/* 2.create struct sockaddr_in */	
	memset(&serv_addr, 0, sizeof(serv_addr));
	serv_addr.sin_family = AF_INET;
	serv_addr.sin_port = htons(SERV_PORT);
	inet_pton(AF_INET, argv[1], &serv_addr.sin_addr.s_addr);

	/* 3.connect() */
	if (connect(cfd, (struct sockaddr *)&serv_addr, sizeof(serv_addr)) == -1)
		perror("connect error..\n");

	/* 4.send() and recv() */
	while (1) {
		printf("Send msg : ");
		fgets(buf, sizeof(buf), stdin);
		
		send(cfd, buf, strlen(buf), 0);
		if (strcmp("exit\n", buf) == 0)
			break;
		
		memset(buf, 0, sizeof(buf));
		
		if (recv(cfd, buf, sizeof(buf), 0) == 0) {
			printf("server exit..\n");
			break;
		}
		printf("Receive msg: %s", buf);
	}

	/* 4.close() */
	close(cfd);

	return 0;
}
