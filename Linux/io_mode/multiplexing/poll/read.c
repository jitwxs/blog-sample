#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <poll.h>

#define NAME "test_fifo"

int main(int argc, char *argv[])
{
	int ret, fd;
	int timeout = 5000; // 设置超时时间，单位为毫秒

	// 创建有名管道
	if (access(NAME, 0) == -1)
		if ((mkfifo(NAME, 0666)) != 0){
			perror("mkfifo");
			exit(1);
		}

	// 读写方式打开管道
	if ((fd = open(NAME, O_RDWR)) < 0){
		perror("open fifo");
		exit(1);
	}

	// 创建struct pollfd 结构体
	struct pollfd pfds[] = {
		{.fd = fd, .events = POLLIN}
	};

	while(1){
		ret = poll(pfds, 1, timeout);
		if (ret == -1)
			perror("poll");
		else if(ret > 0) {
			if (pfds[0].revents == POLLIN) {
				char buf[100] = {0};
				read(fd, buf, sizeof(buf));
				printf("Receive msg: %s\n", buf);

				if (strcmp("quit", buf) == 0)
					break;
			} else
				printf("revents error..\n");
		}else if(ret == 0)
			printf("timeout..\n");
	}

	execlp("rm", "rm", "-f", NAME, NULL);

	return 0;
}
