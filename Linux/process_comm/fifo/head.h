#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>

#define FILE_PATH "./fifo"

#define MAXSIZE 128

// 判断FIFO文件是否存在，存在返回1,否则返回0
int check_fifo_exist() {
	if (!access(FILE_PATH, 0))
		return 1;
	else
		return 0;
}

// 创建FIFO文件
int mk_fifo() {
	return mkfifo(FILE_PATH, 0666);
}

// 删除FIFO文件
int rm_fifo() {
	return execlp("rm", "rm" "-f", FILE_PATH, NULL);
}
