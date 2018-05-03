#include "../head.h"

pthread_spinlock_t lock;
int rear, head, count;
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
	while((n = fread(&buf, sizeof(char), sizeof(buf), fp))) {
		if(rear == head)
			while(count >= QUEUE_SIZE);
		m = queue_buf[rear];
		m->type = MSG_CONTENT;
		m->len = n;
		memcpy(m->data, &buf, m->len);

		rear = (rear + 1) % QUEUE_SIZE;
		pthread_spin_lock(&lock);
		count++;
		pthread_spin_unlock(&lock);
	}

	if (rear == head)
		while(count >= QUEUE_SIZE);
	
	m = queue_buf[rear];
	time(&end_time);
	printf("[INFO] complete send file, Consume %lfs.\n",
			difftime(end_time, start_time));
	if (fp) {
		fclose(fp);
		fp = NULL;
	}
	m->type = MSG_DONE;
	m->len = 0;

	rear = (rear + 1) % QUEUE_SIZE;
	pthread_spin_lock(&lock);
	count++;
	pthread_spin_unlock(&lock);
}

int main(int argc, char *argv[]) {
	int cfd, i;
	struct sockaddr_in serv_addr;
	struct sockaddr_in connect_addr, peer_addr;
	struct msg *send_msg, *rec_msg;
	char connect_ip[BUFSIZ], peer_ip[BUFSIZ];
	char buf[BUFSIZ], source_filename[BUFSIZ], target_filename[BUFSIZ];
	socklen_t connect_len, peer_len;
	pthread_t tid;
	void *status;

	if ((cfd = socket(AF_INET, SOCK_STREAM, 0)) == -1)
		p_error("socket error");

	// malloc struct msg
	for (i=0; i<QUEUE_SIZE; i++)
		queue_buf[i] = (struct msg*)malloc(MSG_SIZE);
	send_msg = (struct msg*)malloc(MSG_SIZE);
	rec_msg = (struct msg*)malloc(MSG_SIZE);

	memset(&serv_addr, 0, sizeof(serv_addr));
	serv_addr.sin_family = AF_INET;
	serv_addr.sin_port = htons(SERV_PORT);
	inet_pton(AF_INET, argv[1], &serv_addr.sin_addr.s_addr);

	if ((i = connect(cfd, (struct sockaddr *)&serv_addr, sizeof(serv_addr))) == -1) {
		if(errno == ECONNREFUSED)
			p_error("connection refuse");
		else if(errno == EHOSTUNREACH)
			p_error("no route to host");
		else if(errno == ETIMEDOUT)
			p_error("connection time out");
	} else
		printf("Connect server succes!\n\n");

	connect_len = sizeof(connect_addr);
	getsockname(cfd, (struct sockaddr *)&connect_addr, &connect_len);
	printf("connect server address = %s:%d\n",
			inet_ntop(AF_INET, &connect_addr.sin_addr.s_addr,
				connect_ip, sizeof(connect_ip)),
			ntohs(connect_addr.sin_port));

	peer_len = sizeof(peer_addr);
	getpeername(cfd, (struct sockaddr *)&peer_addr, &peer_len);
	printf("connect peer address = %s:%d\n",
			inet_ntop(AF_INET, &peer_addr.sin_addr.s_addr,
				peer_ip, sizeof(peer_ip)),
			ntohs(peer_addr.sin_port));

	printf("================\n");

	while (1) {
		printf("please input source file name: ");
		scanf("%s", source_filename);

		// send file name
		memset(send_msg, 0, sizeof(struct msg));
		printf("please input target file name: ");
		scanf("%s", target_filename);

		send_msg->type = MSG_FILENAME;
		send_msg->len = strlen(target_filename);
		memcpy(send_msg->data, &target_filename, send_msg->len);

		send(cfd, (void *)send_msg, sizeof(struct msg) + send_msg->len, 0);
		recv(cfd, (void *)rec_msg, MSG_SIZE, 0);
		if(rec_msg->type == MSG_ERROR) {
			printf("[ERROR] ");
			puts(rec_msg->data);
			exit(1);
		}

		// create thread
		if ((i = pthread_create(&tid, NULL, (void *)thread, source_filename)) != 0)
			p_error("pthread_create");
		
		// init spin lock
		if ((i = pthread_spin_init(&lock, PTHREAD_PROCESS_PRIVATE)) != 0)
			p_error("pthread_spin_init");

		// send file content
		while (1) {
			if(rear == head)
				while(count <= 0);

			struct msg *m = queue_buf[head];
			if(m->type != MSG_CONTENT)
				break;
			send(cfd, (void*)m, sizeof(struct msg) + m->len, 0);
			recv(cfd, (void *)rec_msg, MSG_SIZE, 0);
			if(rec_msg->type == MSG_ERROR) {
				printf("[ERROR] ");
				puts(rec_msg->data);
				exit(1);
			}
			head = (head + 1) % QUEUE_SIZE;
			pthread_spin_lock(&lock);
			count--;
			pthread_spin_unlock(&lock);
		}
		pthread_join(tid, &status);

		if (rear == head)
			while(count <= 0);
		
		struct msg *m = queue_buf[head];
		send(cfd, (void*)m, sizeof(struct msg) + m->len, 0);
		recv(cfd, (void *)rec_msg, MSG_SIZE, 0);
		head = (head + 1) % QUEUE_SIZE;
		pthread_spin_lock(&lock);
		count--;
		pthread_spin_unlock(&lock);

		//exit and continue
loop:	printf(">(input 'q' to quit, input 'c' to continue) ");
		scanf("%s", buf);
		if (strcmp("c", buf) == 0)
			continue;
		else if (strcmp("q", buf) == 0) {
			//send exit
			memset(send_msg, 0, sizeof(struct msg));
			send_msg->type = MSG_EXIT;
			send_msg->len = 0;

			send(cfd, (void*)send_msg, sizeof(struct msg), 0);
			recv(cfd, (void *)rec_msg, MSG_SIZE, 0);

			if (rec_msg->type == MSG_ERROR) {
				printf("[ERROR] ");
				puts(rec_msg->data);
				exit(1);
			} else if (rec_msg->type == MSG_EXIT) {
				printf("[INFO] you will exit..\n");
				close(cfd);
				break;
			}
		} else 
			goto loop;
	}
	return 0;
}
