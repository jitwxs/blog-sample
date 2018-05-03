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
 */
/**********************************************************
 *
 *    TEST IDENTIFIER   : peselet02
 *
 *    TEST TITLE        : Basic tests for pselect(2)
 *
 *    TEST CASE TOTAL   : 3
 *
 *    AUTHOR            : jitwxs
 *						<jitwxs@foxmail.com>
 *
 *    DESCRIPTION
 *		Verify that,
 *		1. pselect() fails with -1 return value and sets errno to EBADF
 *			if a file descriptor that was already closed.
 *		2. pselect() fails with -1 return value and sets errno to EINVAL
 *			if nfds was negative.
 *		3. pselect() fails with -1 return value and sets errno to EINVAL
 *			if the value contained within timeout was invalid.
 *
 **********************************************************/

#include <errno.h>
#include "tst_test.h"

static fd_set read_fds;
static struct timespec time_buf;

static struct tcase {
	int nfds;
	fd_set *readfds;
	struct timespec *timeout;
	int exp_errno;
} tcases[] = {
	{128, &read_fds, NULL, EBADF},
	{-1, NULL, NULL, EINVAL},
	{128, NULL, &time_buf, EINVAL}
};

static void my_test(unsigned int n)
{
	struct tcase *tc = &tcases[n];

	TEST(pselect(tc->nfds, tc->readfds, NULL,
				NULL, tc->timeout, NULL));

	if (TEST_RETURN != -1) {
		tst_res(TFAIL, "pselect() succeeded unexpectedly");
		return;
	}

	if (tc->exp_errno == TEST_ERRNO)
		tst_res(TPASS | TTERRNO, "pselect() failed as expected");
	else
		tst_res(TFAIL | TTERRNO,
				"pselect() failed unexpectedly; expected: %d - %s",
				tc->exp_errno, strerror(tc->exp_errno));
}

static void setup(void)
{
	int fd;

	fd = SAFE_OPEN("test_file", O_RDWR | O_CREAT, 0777);

	FD_ZERO(&read_fds);
	FD_SET(fd, &read_fds);

	SAFE_CLOSE(fd);

	time_buf.tv_sec = -1;
	time_buf.tv_nsec = 0;
}

static struct tst_test test = {
	.tcnt = ARRAY_SIZE(tcases),
	.test = my_test,
	.needs_checkpoints = 1,
	.setup = setup,
	.needs_tmpdir = 1
};
