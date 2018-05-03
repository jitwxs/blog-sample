#include "head.h"

int main(void) {
	int shmid;
	void *shm_addr;
	struct msg *buf;

	// Step1: 创建共享内存
	if ((shmid = shmget(SHM_KEY, SHM_SIZE, IPC_CREAT | 0666)) == -1) {
		printf("创建共享内存失败\n");
		perror("shmget");
	}

	// Step2: 映射共享内存对象
	if ((shm_addr = shmat(shmid, NULL, 0)) == (void *)-1) {
		printf("映射共享内存失败\n");
		perror("shmat");
	}

	buf = (struct msg*)shm_addr;

	// Step3: 写入数据到共享内存
	while(1) {
		printf("send msg: ");
		fgets(buf->msg, MSG_SIZE, stdin);
		buf->flag = 1;
		if (strcmp("exit\n", buf->msg) == 0)
			break;
	}

	// Step4: 分离共享内存
	shmdt(shm_addr);

	// Step5: 删除共享内存
	shmctl(shmid, IPC_RMID, NULL);

	return 0;
}
