#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>

#define PATH "./"

#define QUEUE_ID 200

#define READ_ID 300

#define WRITE_ID 301

#define MSG_SIZE 128

struct msg {
	int type;
	char buf[MSG_SIZE];
};
