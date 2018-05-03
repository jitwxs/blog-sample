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
 *    TEST IDENTIFIER   : read03
 *
 *    TEST TITLE        : Basic tests for read(2)
 *
 *    TEST CASE TOTAL   : 1
 *
 *    AUTHOR            : jitwxs
 *						  <jitwxs@foxmail.com>
 *
 *    DESCRIPTION
 *      Testcase to check that read() sets errno to EAGAIN.
 *
 *    ALGORITHM
 *		Create a named pipe (fifo), open it in O_NONBLOCK mode, and
 *		attempt to read from it, without writing to it. read() should fail
 *		with EAGAIN.
 *
 **********************************************************/

#include <errno.h>
#include "tst_test.h"

#define FIFONAME "tmpFIFO"

int rfd, wfd;

static void my_test(void)
{
	int c;

	TEST(read(rfd, &c, 1));

	if (TEST_RETURN != -1)
		tst_res(TFAIL, "read() failed");

	if (TEST_ERRNO != EAGAIN)
		tst_res(TFAIL, "read set bad errno, expected EAGAIN, got %d",
				TEST_ERRNO);
	else
		tst_res(TPASS, "read() succeded in setting errno to EAGAIN");
}

static void setup(void)
{
	SAFE_MKFIFO(FIFONAME, 0777);
	rfd = SAFE_OPEN(FIFONAME, O_RDONLY | O_NONBLOCK);
	wfd = SAFE_OPEN(FIFONAME, O_WRONLY | O_NONBLOCK);
}

static void cleanup(void)
{
	if (rfd > 0)
		SAFE_CLOSE(rfd);
	if (wfd > 0)
		SAFE_CLOSE(wfd);
}

static struct tst_test test = {
	.test_all = my_test,
	.setup = setup,
	.cleanup = cleanup,
	.needs_tmpdir = 1
};
