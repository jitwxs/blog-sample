#include "head.h"

int main(void) {

	int lfd, i;
	char buf[BUFSIZ], clie_ip[BUFSIZ];
	struct sockaddr_in serv_addr, clie_addr;
	socklen_t clie_addr_len;
	ssize_t n;

	/* 1.socket() */
	if ((lfd = socket(AF_INET, SOCK_DGRAM, 0)) == -1)
		p_error("socket error..");

	/* 2.create struct sockaddr_in */	
	memset(&serv_addr, 0, sizeof(serv_addr));
	serv_addr.sin_family = AF_INET;
	serv_addr.sin_port = htons(SERV_PORT);
	serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);
	
	/* 3.bind() */
	if (bind(lfd, (struct sockaddr *)&serv_addr, sizeof(serv_addr)) == -1)
		p_error("bind error..");

	/* 4.recvfrom() and sendto() */
	while(1) {
		clie_addr_len = sizeof(clie_addr);

		if ((n = recvfrom(lfd, buf, sizeof(buf), 0, (struct sockaddr *)&clie_addr, &clie_addr_len)) == -1)
			p_error("recvfrom error..");
		
		printf("Receive msg[%s:%d]: %s",
				inet_ntop(AF_INET, &clie_addr.sin_addr.s_addr,
					clie_ip, sizeof(clie_ip)),
				ntohs(clie_addr.sin_port), buf);
		
		if(strcmp("exit\n", buf) == 0) {
			printf("[%s:%d] exit..\n",
					clie_ip, ntohs(clie_addr.sin_port));
			continue;
		}

		for(i = 0; i < n; i++)
			buf[i] = toupper(buf[i]);
		
		printf("Send msg: %s",buf);
		
		if (sendto(lfd, buf, strlen(buf), 0, (struct sockaddr *)&clie_addr, sizeof(clie_addr)) == -1)
			p_error("sendto error..");
		
		memset(buf, 0, sizeof(buf));
	}

	/* 4.close() */
	close(lfd);
}
