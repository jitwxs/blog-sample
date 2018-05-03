#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <sys/wait.h>
#include <sys/types.h>

#define SHM_SIZE 4096

#define MSG_SIZE 128

#define SHM_KEY 2017

struct msg {
	char msg[MSG_SIZE];
	int flag;
};
