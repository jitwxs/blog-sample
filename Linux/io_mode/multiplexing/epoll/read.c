#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/epoll.h>

#define MAXEVENTS 1024 //最大事件数目
#define NAME "test_fifo"

int main(int argc, char *argv[])
{
	int res, fd, epfd;
	struct epoll_event events[MAXEVENTS]; //监听事件数组
	struct epoll_event ev; //监听事件临时变量
	int timeout = 5000; // 设置超时时间，单位为毫秒

	// 创建有名管道
	if (access(NAME, 0) == -1)
		if ((mkfifo(NAME, 0666)) != 0){
			perror("mkfifo");
			exit(1);
		}

	// 读写方式打开管道
	if ((fd = open(NAME, O_RDWR)) < 0){
		perror("open");
		exit(1);
	}

	// 设置监听的事件内容
	ev.data.fd = fd;
	ev.events = EPOLLIN;

	// 创建epoll句柄
	epfd = epoll_create(MAXEVENTS);

	// 注册事件ev
	if (epoll_ctl(epfd, EPOLL_CTL_ADD, fd, &ev) == -1) {
		perror("epoll_ctl");
		return -1;
	}

	while(1){
		// 等待事件
		res = epoll_wait(epfd, events, MAXEVENTS, timeout);
		if (res == -1)
			perror("epoll_wait");
		else if (res == 0)
			printf("timeout..\n");
		else {
			// 循环读取
			for(int i=0; i<res; i++) {
				if (events[i].data.fd == fd && events[i].events & EPOLLIN) {
					char buf[100] = {0};
					read(fd, buf, sizeof(buf)); 
					printf("Receive msg: %s\n", buf);
				}
			}
		}
	}

	return 0;
}
