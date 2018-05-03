#include "head.h"

static pthread_mutex_t mutex;

int rear, head, count;
struct msg *queue_buf[QUEUE_SIZE];

char client_ip[48];
int client_port;

void thread(char *filename) {
	FILE *fp;
	time_t start_time, end_time;
	int flag = 1;

	if((fp = fopen(filename, "w+")) == NULL)
		p_error("fopen");

	time(&start_time);

	while (flag) {
		if (rear == head)
			while (count <= 0) {
				usleep(100);		
			}
		struct msg *m = queue_buf[head];
		switch (m->type) {
			case MSG_DONE:
				if (fp) {
					fclose(fp);
					fp = NULL;
				}
				time(&end_time);
				printf("[INFO] Complete save file\n");
				printf("[INFO] Client : %s:%d\n",client_ip, client_port);
				printf("[INFO] Save path : %s\n",filename);
				printf("[INFO] Consume : %lfs\n", difftime(end_time ,start_time));
				printf("============\n");

				head = ( head + 1) % QUEUE_SIZE;
				pthread_mutex_lock(&mutex);
				count--;
				pthread_mutex_unlock(&mutex);

				flag = 0;
				break;
			case MSG_CONTENT:
				fwrite(m->data, sizeof(char), m->len, fp);
				fflush(fp);

				head = ( head + 1) % QUEUE_SIZE;
				pthread_mutex_lock(&mutex);
				count--;
				pthread_mutex_unlock(&mutex);
				break;
			default: 
				flag = 0;
				break;
		}
	}
	pthread_exit(NULL);
}

int main(void) {
	int lfd, i;
	struct sockaddr_in serv_addr, clie_addr;
	struct msg *send_msg, *rec_msg;
	char filename[BUFSIZ];
	socklen_t clie_len;
	pthread_t tid;

	if ((lfd = socket(AF_INET, SOCK_DGRAM, 0))  == -1)
		p_error("socket error");

	// malloc struct msg
	for (i=0; i<QUEUE_SIZE; i++)
		queue_buf[i] = (struct msg*)malloc(MSG_SIZE);
	send_msg = (struct msg*)malloc(MSG_SIZE);
	rec_msg = (struct msg*)malloc(MSG_SIZE);

	memset(&serv_addr, 0, sizeof(serv_addr));
	serv_addr.sin_family = AF_INET;
	serv_addr.sin_port = htons(SERV_PORT);
	serv_addr.sin_addr.s_addr = htonl(INADDR_ANY); 

	int opt = 1;
	setsockopt(lfd, SOL_SOCKET, SO_REUSEADDR,&opt, sizeof(opt));

	if ((i = bind(lfd, (struct sockaddr *)&serv_addr, sizeof(serv_addr))) == -1)
		p_error("bind error");

	printf("Server start service..\n");
	while (1) {
		clie_len = sizeof(clie_addr);
		memset(send_msg, 0, sizeof(struct msg));

		recvfrom(lfd, (void*)rec_msg, MSG_SIZE, 0, (struct sockaddr *)&clie_addr, &clie_len);
		rec_msg->data[rec_msg->len] = 0;

		inet_ntop(AF_INET, &clie_addr.sin_addr.s_addr, client_ip, sizeof(client_ip));
		client_port =  ntohs(clie_addr.sin_port);

		switch (rec_msg->type) {
			case MSG_FILENAME:
				strcpy(filename, rec_msg->data);

				if ((i = pthread_create(&tid, NULL, (void *)thread, filename)) != 0)
					p_error("pthread_create");
				break;
			case MSG_CONTENT:
				if (rear == head)
					while (count >= QUEUE_SIZE);

				queue_buf[rear]->type = rec_msg->type;
				queue_buf[rear]->len = rec_msg->len;
				memcpy(queue_buf[rear]->data, rec_msg->data, rec_msg->len);

				rear = (rear + 1) % QUEUE_SIZE;
				pthread_mutex_lock(&mutex);
				count++;
				pthread_mutex_unlock(&mutex);

				break;
			case MSG_DONE:
				if (rear == head)
					while (count >= QUEUE_SIZE);

				queue_buf[rear]->type = rec_msg->type;
				queue_buf[rear]->len = rec_msg->len;
				memcpy(queue_buf[rear]->data, rec_msg->data, rec_msg->len);

				rear = (rear + 1) % QUEUE_SIZE;

				pthread_mutex_lock(&mutex);
				count++;
				pthread_mutex_unlock(&mutex);

				break;
			default: 
				break;
		}
	}
	return 0;
}
