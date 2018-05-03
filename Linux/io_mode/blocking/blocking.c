#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <string.h>
#include <errno.h>

#define BUFSIZE 128

int main() {
	char buf[BUFSIZE] = {0};
	int ret, flags;

	/* 注：标准输入输出默认就是阻塞IO，其实可以不用手动设置 */

	// 获得输入的文件状态标记
	if  ((flags = fcntl(STDIN_FILENO, F_GETFL, 0)) < 0) {
		perror("fcntl");
		return -1;
	}

	// 设置为阻塞IO
	flags &= ~O_NONBLOCK;
	if (fcntl(STDIN_FILENO, F_SETFL, flags) < 0) {
		perror("fcntl");
		return -1;
	}

	while (1) {
		sleep(2);

		// 标准输入
		ret = read(STDIN_FILENO, buf, BUFSIZE-1);
		if (ret == 0)
			perror("read--no");
		else if (ret == -1)
			printf("[ERROR] %s\n", strerror(errno));
		else
			printf("read = %d\n", ret);

		// 标准输出
		write(STDOUT_FILENO, buf, BUFSIZE);
		memset(buf, 0, BUFSIZE);
	}

	return 0;
}
