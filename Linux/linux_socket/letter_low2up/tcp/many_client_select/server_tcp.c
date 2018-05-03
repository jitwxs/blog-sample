#include "../head.h"

struct sockaddr_in peer_addr;
char peer_ip[BUFSIZ];
socklen_t peer_len;

// 处理数据，正常结束返回0，用户退出返回1
int parser_msg(int cfd) {
	char buf[BUFSIZ];
	time_t t;
	int i;

	// 接收数据
	if ((recv(cfd, &buf, sizeof(buf), 0)) <= 0) {
		printf("client exit..\n");
		return 1;
	}

	time(&t);
	printf("%s", ctime(&t));
	printf("Recvice: [%s:%d]: %s", peer_ip, ntohs(peer_addr.sin_port), buf);

	if (strcmp("exit\n", buf) == 0) {
		printf("%s:%d exit now!\n", peer_ip, ntohs(peer_addr.sin_port));
		return 1;
	}

	for (i = 0; i < strlen(buf); i++)
		buf[i] = toupper(buf[i]);

	// 发送数据
	printf("Send: [%s:%d]: %s\n", peer_ip, ntohs(peer_addr.sin_port), buf);
	send(cfd, &buf, sizeof(buf), 0);

	return 0;
}

int main(void) {
	int i, opt, maxi, maxfd;
	int lfd, cfd;
	int nready, client[FD_SETSIZE]; /* 自定义数组，用于存放所有连接的客户端*/
	struct sockaddr_in serv_addr, clie_addr;
	struct sockaddr_in listen_addr;
	char listen_ip[BUFSIZ];
	struct timeval tv;
	fd_set rset, allset; /* rset读事件文件描述符集合 allset用来暂存*/
	socklen_t clie_len, listen_len, peer_len;

	if ((lfd = socket(AF_INET, SOCK_STREAM, 0)) == -1)
		p_error("socket error");

	// 初始化serv_addr
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
		perror("listen error");

	listen_len = sizeof(listen_addr);
	getsockname(lfd, (struct sockaddr *)&listen_addr, &listen_len);
	printf("server listen address = %s:%d\n",
			inet_ntop(AF_INET, &listen_addr.sin_addr.s_addr,
				listen_ip, sizeof(listen_ip)),
			ntohs(listen_addr.sin_port));

	printf("Server service start success!\n\n");

	/* ----以上代码完成服务端服务启动，下面开始处理客户端连接---- */

	maxfd = lfd; /* 起初lfd为最大文件描述符 */
	maxi = -1; /* client数组下标初始化为-1 */

	memset(client, -1, FD_SETSIZE); /* 初始化client数组为-1 */

	FD_ZERO(&allset);
	FD_SET(lfd, &allset); /* 将lfd加入监听集 */

	while (1) {
		rset = allset; /* 每次循环重新设置select监听集 */
		tv.tv_sec = 3; /* 每次循环重新设置超时 */
		
		nready = select(maxfd+1, &rset, NULL, NULL, &tv); /* select监听 */

		if (nready < 0)
			p_error("select error");
		else if (nready == 0) {
			printf("timeout..\n");
			continue;
		}

		// 处理新客户端登录
		if (FD_ISSET(lfd, &rset)) {
			clie_len = sizeof(clie_addr);

			if ((cfd = accept(lfd, (struct sockaddr *)&clie_addr, &clie_len)) == -1) /* 此时accept一定会阻塞 */
				p_error("accept error");

			// 打印登录信息
			peer_len = sizeof(peer_addr);
			getpeername(cfd, (struct sockaddr *)&peer_addr, &peer_len);
			printf("%s:%d login now!\n\n",
					inet_ntop(AF_INET, &peer_addr.sin_addr.s_addr,
						peer_ip, sizeof(peer_ip)),
					ntohs(peer_addr.sin_port));

			// 将cfd存入client数组中，并更新client数组最大下标
			for (i=0; i<FD_SETSIZE; i++)
				if (client[i] < 0) {
					client[i] = cfd;
					if (i > maxi)
						maxi = i;
					break;
				}

			// 达到监听上限
			if (i == FD_SETSIZE) {
				puts("too many clients\n");
				exit(EXIT_FAILURE);
			}

			FD_SET(cfd, &allset); /* 将cfd添加入allset监听集中 */

			if (cfd > maxfd) /* 更新maxfd */
				maxfd = cfd;
			
			if (--nready <= 0) /* 如果仅仅接收到新客户端登录，则continue */
				continue;
		}

		// 接收客户端发送的数据
		for (i=0; i<=maxi; i++) {
			if ((cfd = client[i]) == -1)
				continue;
			if (FD_ISSET(cfd, &rset)) {
				// 客户端退出
				if (parser_msg(cfd) == 1) {
					close(cfd); /* 关闭文件描述符 */
					FD_CLR(cfd, &allset); /* 从监听集中移除 */
					client[i] = -1; /* 从client数组移除 */
				}
			}
		}
	} 

	return 0;
}
