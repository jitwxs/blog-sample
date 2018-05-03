#include "head.h"

int main(int argc, char *argv[])
{
	int fd;
	char buf[BUFSIZE];

	// 创建有名管道
	if (!check_fifo_exist())
		if (mk_fifo() == -1) {
			printf("创建FIFO文件出错");
			perror("mkfifo");
		}

	// 读写方式打开管道
	if ((fd = open(FILE_PATH, O_RDWR)) < 0){
		printf("打开FIFO文件出错");
		perror("open");
	}

	while(1){
		printf("Send msg: ");
		fgets(buf, BUFSIZE, stdin);
		write(fd, buf, strlen(buf)); // 往管道里写内容

		if(strcmp("exit\n", buf) == 0)
			break;
	}

	close(fd);
	if (check_fifo_exist())
		if (rm_fifo() == -1) {
			printf("删除FIFO文件出错");
			perror("rmfifo");
		}

	return 0;
}
