#include "../head.h"

int main(int len, char *argv[]) {
	int cfd;
	struct sockaddr_in serv_addr;
	struct sockaddr_in connect_addr, peer_addr;
	char connect_ip[BUFSIZ], peer_ip[BUFSIZ];
	char buf[BUFSIZ];
	time_t t;
	socklen_t connect_len, peer_len;

    if ((cfd = socket(AF_INET, SOCK_STREAM, 0)) == -1)
		p_error("socket error");

	memset(&serv_addr, 0, sizeof(serv_addr));
	serv_addr.sin_family = AF_INET;
	serv_addr.sin_port = htons(SERV_PORT);
	inet_pton(AF_INET, argv[1], &serv_addr.sin_addr.s_addr);

	if (connect(cfd, (struct sockaddr *)&serv_addr, sizeof(serv_addr)) == -1)
		switch (errno) {
			case ECONNREFUSED:
				p_error("connection refuse");
			case EHOSTUNREACH:
				p_error("no route to host");
			case ETIMEDOUT:
				p_error("connection time out");
			default:
				p_error("unknown");
		}
	else
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
		time(&t);
		printf("%s Send msg: ", ctime(&t));
		
		fgets(buf, sizeof(buf), stdin);
		
		send(cfd, &buf, strlen(buf), 0);
		if(strcmp("exit\n", buf) == 0)
			break;
	
		memset(&buf, 0, sizeof(buf));

		if (recv(cfd, &buf, sizeof(buf), 0) == 0) {
			printf("server exit..\n");
			break;
		}
		time(&t);
		printf("\n%s Receive msg: %s\n", ctime(&t), buf);
	}

	close(cfd);
	return 0;
}
