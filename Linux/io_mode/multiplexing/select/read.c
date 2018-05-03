#include "head.h"

int main(int argc, char *argv[])
{
	fd_set rfds;
	struct timeval tv;
	int ret, fd;

	// 创建有名管道
	if (!check_fifo_exist())
		if (mk_fifo() == -1) {
			printf("创建FIFO文件出错");
			perror("mkfifo");
		}

	// 读写方式打开管道
	if ((fd = open(FILE_PATH, O_RDWR)) == -1){
		printf("打开FIFO文件出错");
		perror("open");
	}

	while(1){
		//每次循环都要初始化
		FD_ZERO(&rfds);		// 清空
		FD_SET(fd, &rfds);  // 有名管道描述符 fd 加入集合

		// 超时设置
		tv.tv_sec = 5;
		tv.tv_usec = 0;

		// 监视并等待多个文件（标准输入，有名管道）描述符的属性变化（是否可读）
		// 没有属性变化，这个函数会阻塞，直到有变化才往下执行，这里没有设置超时
		// FD_SETSIZE 为 <sys/select.h> 的宏定义，值为 1024
		if ((ret = select(FD_SETSIZE, &rfds, NULL, NULL, &tv)) == -1)
			perror("select");
		else if(ret > 0) {
			char buf[100] = {0};
			read(fd, buf, sizeof(buf));
			printf("Receive msg: %s", buf);

			if (strcmp("exit\n", buf) == 0)
				break;
		}else if(ret == 0)
			printf("Time out..\n");
	}

	close(fd);
	if (check_fifo_exist())
		if (rm_fifo() == -1) {
			printf("删除FIFO文件出错");
			perror("rmfifo");
		}

	return 0;
}
