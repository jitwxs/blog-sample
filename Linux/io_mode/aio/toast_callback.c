#include<stdio.h>
#include<string.h>
#include<stdlib.h>
#include<unistd.h>
#include<errno.h>
#include <aio.h>
#include<fcntl.h>

#define BUFSIZE 100

void call_back_handler(sigval_t sigval){
	struct aiocb *req;

	req = (struct aiocb*)sigval.sival_ptr;
	if (aio_error(req) == 0) {
		printf("================\n");
		printf("call back handler\n");
		printf("print from callback:\n%s",(char*)req->aio_buf);
		printf("================\n");
	}
}

int main(void) {
	int fd, i, ret;
	struct aiocb my_aiocb;

	if ((fd = open("test.txt",O_RDONLY))<0)
		perror("open");

	 /* Set up the AIO request */
	bzero((char *)&my_aiocb,sizeof(struct aiocb));
	my_aiocb.aio_buf = malloc(BUFSIZE + 1);
	my_aiocb.aio_fildes = fd;
	my_aiocb.aio_nbytes = BUFSIZE;
	my_aiocb.aio_offset = 0;

	/* Link the AIO request with a thread callback */
	my_aiocb.aio_sigevent.sigev_notify = SIGEV_THREAD;
	my_aiocb.aio_sigevent.sigev_notify_function = call_back_handler;
	my_aiocb.aio_sigevent.sigev_notify_attributes = NULL;
	my_aiocb.aio_sigevent.sigev_value.sival_ptr = &my_aiocb;

	if ((ret = aio_read(&my_aiocb))<0)
		perror("aio_read");

	i = 0;
	while (i++ < 5) {
		printf("in main dead loop..\n");
		sleep(1);
	}

	return 0;
}
