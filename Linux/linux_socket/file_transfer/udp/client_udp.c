#include "head.h"

pthread_spinlock_t lock;
int rear, head, count;
double use_time;
struct msg *queue_buf[QUEUE_SIZE];

void thread(char *filename) {
	FILE *fp;
	char buf[BUFSIZ];
	ssize_t n;
	time_t start_time, end_time;
	struct msg *m;

	if (!(fp = fopen(filename, "r")))
		p_error("fopen");

	time(&start_time);
	while (1) {
		if(rear == head)
			while(count >= QUEUE_SIZE);

		m = queue_buf[rear];

		memset(&buf, 0, sizeof(buf));
		n = fread(&buf, sizeof(char), sizeof(buf), fp);
		if (n == 0) {
			if (fp) {
				fclose(fp);
				fp = NULL;
			}
			time(&end_time);
			use_time = difftime(end_time, start_time);
			
			m->type = MSG_DONE;
			m->len = 0;
			m->data[0] = '\0';

			rear = (rear + 1) % QUEUE_SIZE;
			pthread_spin_lock(&lock);
			count++;
			pthread_spin_unlock(&lock);
			break;
		} else {
			m->type = MSG_CONTENT;
			m->len = n;
			memcpy(m->data, &buf, m->len);

			rear = (rear + 1) % QUEUE_SIZE;
			pthread_spin_lock(&lock);
			count++;
			pthread_spin_unlock(&lock);
		}
	}
	pthread_exit(NULL);
}

int main(int argc, char *argv[]) {
	int cfd, i;
	struct sockaddr_in serv_addr;
	struct msg *send_msg;
	char buf[BUFSIZ], source_filename[BUFSIZ], target_filename[BUFSIZ];
	pthread_t tid;

	if ((cfd = socket(AF_INET, SOCK_DGRAM, 0)) == -1)
		p_error("socket error");

	send_msg = (struct msg*)malloc(MSG_SIZE);
	memset(&serv_addr, 0, sizeof(serv_addr));
	serv_addr.sin_family = AF_INET;
	serv_addr.sin_port = htons(SERV_PORT);
	inet_pton(AF_INET, argv[1], &serv_addr.sin_addr.s_addr);

	printf("Client UDP service start..\n");

	while (1) {
		// malloc struct msg
		for (i=0; i<QUEUE_SIZE; i++)
			queue_buf[i] = (struct msg*)malloc(MSG_SIZE);

		printf("please input source file name: ");
		scanf("%s", source_filename);

		printf("please input target file name: ");
		scanf("%s", target_filename);

		memset(send_msg, 0, sizeof(struct msg));
		send_msg->type = MSG_FILENAME;
		send_msg->len = strlen(target_filename);
		memcpy(send_msg->data, &target_filename, send_msg->len);

		// send file name
		sendto(cfd, (void *)send_msg, sizeof(struct msg) + send_msg->len, 0,
				(struct sockaddr *)&serv_addr, sizeof(serv_addr));

		// create thread
		if ((i = pthread_create(&tid, NULL, (void *)thread, source_filename)) != 0)
			p_error("pthread_create");

		// init spin lock
		if ((i = pthread_spin_init(&lock, PTHREAD_PROCESS_PRIVATE)) != 0)
			p_error("pthread_spin_init");

		// send file content
		while (1) {
			if (rear == head)
				while (count <= 0);

			struct msg *m = queue_buf[head];

			sendto(cfd, (void*)m, sizeof(struct msg) + m->len, 0,
					(struct sockaddr *)&serv_addr, sizeof(serv_addr));

			if (m->type == MSG_DONE) {
				printf("[INFO] complete send file, consume %lfs.\n", use_time);
				break;	
			}

			head = (head + 1) % QUEUE_SIZE;
			pthread_spin_lock(&lock);
			count--;
			pthread_spin_unlock(&lock);
		}

		//exit and continue
loop:	printf(">(input 'q' to quit, input 'c' to continue) ");
		scanf("%s", buf);
		if (strcmp("c", buf) == 0)
			continue;
		else if (strcmp("q", buf) == 0) {
			printf("[INFO] you will exit..\n");
			close(cfd);
			break;
		} else 
			goto loop;
	}
	return 0;
}
