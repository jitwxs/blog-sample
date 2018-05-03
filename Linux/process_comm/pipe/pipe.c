#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

#define MAXSIZE 128

int main(void) {
    int fd[2];
	pid_t pid;

	if (pipe(fd) != 0) {
		printf("创建无名管道失败\n");
		perror("pipe");
	}

	if ((pid = fork()) == -1) {
		printf("创建子进程失败\n");
		perror("fork");
	} else if (pid == 0) { // 子进程发送数据
		char msg[MAXSIZE];
		printf("send msg: ");
		fgets(msg, MAXSIZE, stdin);
		close(fd[0]);
		write(fd[1], msg, strlen(msg));
	} else { // 父进程接收数据
		char rec[MAXSIZE];
		wait(NULL);
		close(fd[1]);
		read(fd[0], rec, sizeof(rec));
		printf("receive msg: %s", rec);
	}
    
	return 0;
}
