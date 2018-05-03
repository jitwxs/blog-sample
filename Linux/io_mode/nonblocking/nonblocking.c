#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <fcntl.h>
#include <string.h>
#include <errno.h>

#define BUFSIZE 128

int main() {
	char buf[BUFSIZE] = {0};
	int ret, flags;

	// 获得输入的文件状态标记
	if ((flags = fcntl(STDIN_FILENO, F_GETFL, 0)) < 0) {
		perror("fcntl");
		return EXIT_FAILURE;
	}

	// 设置为非阻塞IO
	flags |= O_NONBLOCK;
	if (fcntl(STDIN_FILENO, F_SETFL, flags) < 0) {
		perror("fcntl");
		return EXIT_FAILURE;
	}

	while (1) {
		sleep(2);
		
		// 标准输入
		ret = read(STDIN_FILENO, buf, BUFSIZE-1);
		if (ret == 0)
			perror("read--no");
		else if (ret == -1) {
			if (errno == EWOULDBLOCK)
				printf("Data is not ready!\n");
			else
				printf("[ERROR] %s\n", strerror(errno));
		} else
			printf("read = %d\n", ret);

		// 标准输出
		write(STDOUT_FILENO, buf, BUFSIZE);
		memset(buf, 0, BUFSIZE);
	}

	return 0;
}
