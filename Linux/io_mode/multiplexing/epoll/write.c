#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/select.h>

#define BUFSIZE 128
#define NAME "test_fifo"

int main(int argc, char *argv[])
{
	int fd;
	char buf[BUFSIZE];

	// 创建有名管道
	if (access(NAME, 0) == -1)
		if ((mkfifo(NAME, 0666)) != 0) {
			perror("mkfifo");
			return -1;
		}

	// 读写方式打开管道
	if((fd = open("test_fifo", O_RDWR)) < 0){
		perror("open fifo");
		return -1;
	}

	// 往管道里写内容
	while(1){
		printf("Send msg: ");
		scanf("%s", buf);
		write(fd, buf, strlen(buf));
	}
	return 0;
}
