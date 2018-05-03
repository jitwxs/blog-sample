#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/shm.h>  
#include <sys/sem.h>
#include <sys/ipc.h>
#include <sys/wait.h>

#define TIMES 5
#define FLAG (IPC_CREAT | 0666)
#define MAX_BUFFER_SIZE 3

typedef struct {
	int bottom;
	int top;
	int data[MAX_BUFFER_SIZE];
}BUFFER;

BUFFER *pBuffer = NULL;
int sem_consume;
int sem_produce;
int shm_buffer;

void init() {
	union semun {
		int val;
		struct semid_ds *buf;
		unsigned short *array;
	}arg;

	// Step1: 创建并映射共享内存
	shm_buffer = shmget(IPC_PRIVATE, sizeof(BUFFER), FLAG);
	pBuffer = shmat(shm_buffer, 0, 0);
	memset(pBuffer,0,sizeof(BUFFER));

	// Step2: 创建消费者和生产者信号量
	sem_consume = semget(IPC_PRIVATE, 1, FLAG);
	arg.val = 0;
	semctl(sem_consume, 0, SETVAL, arg);

	sem_produce = semget(IPC_PRIVATE, 1, FLAG);
	arg.val = MAX_BUFFER_SIZE;
	semctl(sem_produce, 0, SETVAL, arg);
}

void destory() {
	// Step4: 分离并删除共享内存
	shmdt(pBuffer);
	shmctl(shm_buffer, IPC_RMID, NULL);
	
	// Step5: 删除信号量
	semctl(sem_consume,0,IPC_RMID);
	semctl(sem_produce,0,IPC_RMID);
}

int main(void) {
	int pid,i;
	struct sembuf sbuf;

	init();
	// Step3: 生产者进程中做V操作，消费者进程中做P操作
	if ((pid = fork()) > 0) {
		for(i=0; i<TIMES; i++) {
			sbuf.sem_num = 0;
			sbuf.sem_op = -1; // 做P操作
			sbuf.sem_flg = 0;
			
			semop(sem_consume,&sbuf,1);
			system("date | awk '{print $4}'");
			printf("consumer get %6d,pos=%d\n",
					pBuffer->data[pBuffer->bottom], pBuffer->bottom);
			pBuffer->bottom = (pBuffer->bottom+1) % MAX_BUFFER_SIZE;
			semop(sem_produce,&sbuf,1);
			
			sleep(2);
		}
		wait(NULL);
	} else if (pid == 0) {
		for(i=0; i<TIMES; i++){
			sbuf.sem_num = 0;
			sbuf.sem_op = 1; // 做V操作
			sbuf.sem_flg = 0;
			
			semop(sem_produce,&sbuf,1);
			pBuffer->data[pBuffer->top] = (rand()%100)+i+1;
			system("date | awk '{print $4}'");
			printf("produce put %6d,pos=%d\n",
					pBuffer->data[pBuffer->top], pBuffer->top);
			pBuffer->top=(pBuffer->top+1)%MAX_BUFFER_SIZE;
			semop(sem_consume,&sbuf,1);
			
			sleep(1);
		}
		exit(0);
	}

	destory();
	return 0;
}
