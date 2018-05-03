#include "head.h"

int main(int argc, char *argv[]) {
	int cfd;
	struct sockaddr_in serv_addr;
	ssize_t n;
	char buf[BUFSIZ];

	/* 1.socket() */
	if ((cfd = socket(AF_INET, SOCK_DGRAM, 0)) == -1)
		p_error("socket error..");

	/* 2.create struct sockaddr_in */	
	memset(&serv_addr, 0, sizeof(serv_addr));
	serv_addr.sin_family = AF_INET;
	serv_addr.sin_port = htons(SERV_PORT);
	inet_pton(AF_INET, argv[1], &serv_addr.sin_addr.s_addr);

	/* 3.sendto() and recvfrom() */
	while (fgets(buf, sizeof(buf), stdin) != NULL) {
		printf("Send msg : %s", buf);
		
		if ((n = sendto(cfd, buf, strlen(buf), 0, (struct sockaddr *)&serv_addr, sizeof(serv_addr))) == -1)
			p_error("sendto error..");
	
		if(strcmp("exit\n", buf) == 0)
			break;

		n = recvfrom(cfd, buf, sizeof(buf), 0, NULL, 0);
		if (n == -1)
			p_error("recvfrom error..");
		else if (n == 0) {
			printf("server exit..\n");
			break;
		}
		
		printf("Receive msg: %s", buf);
		
		memset(buf, 0, sizeof(buf));
	}

	/* 4.close() */
	close(cfd);

	return 0;
}
