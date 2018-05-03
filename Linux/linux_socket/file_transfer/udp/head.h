#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/time.h>
#include <arpa/inet.h>
#include <errno.h>
#include <time.h>
#include <sys/wait.h>
#include <pthread.h>
#include <semaphore.h>

#define SERV_PORT 2017
#define BACKLOG 10
#define QUEUE_SIZE 5

#define MSG_FILENAME 1
#define MSG_CONTENT 2
#define MSG_ACK 3
#define MSG_DONE 4
#define MSG_ERROR 5
#define MSG_EXIT 6

#define MSG_SIZE BUFSIZ + 2*sizeof(int)

struct msg {
	int type;
	int len;
	char data[];
};

void p_error(char *msg) {
	perror(msg);
	exit(EXIT_FAILURE);
}
