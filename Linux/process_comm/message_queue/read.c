#include "head.h"

int main(void) {
	key_t key;
	int msgid;
	struct msg my_msg;

	// Step1: 创建消息队列
	key = ftok(PATH, QUEUE_ID);
	if ((msgid = msgget(key, IPC_CREAT | 0666)) < 0) {
		printf("创建消息队列失败\n");
		perror("msgget");
	}

	my_msg.type = READ_ID;
	while(1) {
		// Step2: 从消息队列中读取消息
		msgrcv(msgid, &my_msg, MSG_SIZE, 0, 0);
		if (strcmp("exit\n", my_msg.buf) == 0)
			break;
		printf("rec msg: %s", my_msg.buf);
	}

	//Step3: 删除消息队列
	msgctl(msgid, IPC_RMID, 0);

    return 0;
}
