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

	my_msg.type = WRITE_ID;
	while(1) {
		printf("send msg: ");
		fgets(my_msg.buf, MSG_SIZE, stdin);

		// Step2: 发送消息到消息队列
		msgsnd(msgid, &my_msg, strlen(my_msg.buf), 0);

		if (strcmp("exit\n", my_msg.buf) == 0)
			break;
	}

	//Step3: 删除消息队列
	msgctl(msgid, IPC_RMID, 0);

    return 0;
}
