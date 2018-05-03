/*
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of version 2 of the GNU General Public License as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it would be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write the Free Software Foundation, Inc.
 *
 */
/**********************************************************
 *
 *    TEST IDENTIFIER   : select05
 *
 *    TEST TITLE        : Basic tests for select(2)
 *
 *    TEST CASE TOTAL   : 1
 *
 *    AUTHOR            : jitwxs
 *						  <jitwxs@foxmail.com>
 *
 *    DESCRIPTION
 *		Check that select() read and write monitoring correctly.
 *
 **********************************************************/

#include <stdlib.h>
#include <errno.h>
#include "tst_test.h"

int fd[2];

static void my_test(void)
{
	struct timeval tv;
	int retval;
	fd_set fs;

	FD_ZERO(&fs);
	FD_SET(fd[0], &fs);
	FD_SET(fd[1], &fs);

	tv.tv_sec = 3;
	tv.tv_usec = 0;

	if (SAFE_FORK() == 0) {
		close(fd[0]);
		write(fd[1], "test", strlen("test"));

		exit(EXIT_SUCCESS);
	}

	retval = select(FD_SETSIZE, &fs, &fs, NULL, &tv);

	SAFE_WAIT(NULL);

	if (retval == -1)
		tst_res(TFAIL | TERRNO, "error: %s", strerror(errno));
	else if (retval && !FD_ISSET(fd[0], &fs) &&
			FD_ISSET(fd[1], &fs))
		tst_res(TPASS, "select() pass");
	else
		tst_res(TFAIL, "select() failed");
}

void setup(void)
{
	if ((pipe(fd)) < 0)
		tst_brk(TBROK | TERRNO, "pipe error");
}

static struct tst_test test = {
	.test_all = my_test,
	.setup = setup,
	.forks_child = 1
};
