#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <aio.h>
#include <unistd.h>

#define FILE_NAME "test.txt"

int main(void) {
	int fd, ret;
	struct aiocb my_aiocb;
	
	fd = open(FILE_NAME, O_RDONLY);
	if (fd < 0)
		perror("open error");

	/* Zero out the aiocb structure (recommended) */
	memset(&my_aiocb, 0, sizeof(my_aiocb));
	
	/* Allocate a data buffer for the aiocb request */
	my_aiocb.aio_buf = malloc(BUFSIZ+1);
	if (!my_aiocb.aio_buf)
		perror("malloc error");

	/* Initialize the necessary fields in the aiocb */
	my_aiocb.aio_fildes = fd;
	my_aiocb.aio_nbytes = BUFSIZ;
	my_aiocb.aio_offset = 0;

	if (aio_read(&my_aiocb) < 0)
		perror("aio_read");
	
	/* loop wait read data */
	while (aio_error(&my_aiocb) == EINPROGRESS) {
		printf("wait read...\n");
		sleep(1);	
	}

	/* get return value */
	if ((ret = aio_return(&my_aiocb)) > 0)
		printf("res = %d, content = %s\n",
				ret, (char *)my_aiocb.aio_buf);
	else
		perror("ail_return");

    return 0;
}
