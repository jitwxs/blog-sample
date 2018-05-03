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
 *    TEST IDENTIFIER   : open04
 *
 *    TEST TITLE        : Basic tests for open(2)
 *
 *    TEST CASE TOTAL   : 1
 *
 *    AUTHOR            : jitwxs
 *						  <jitwxs@foxmail.com>
 *
 *    DESCRIPTION
 *		Testcase to check that open(2) sets EMFILE if a process opens files
 *		more than its descriptor size
 *
 *    ALGORITHM
 *		First get the file descriptor table size which is set for a process.
 *		Use open(2) for creating files till the descriptor table becomes full.
 *		These open(2)s should succeed. Finally use open(2) to open another
 *		file. This attempt should fail with EMFILE.
 *
 **********************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <sys/resource.h>
#include <fcntl.h>
#include <unistd.h>
#include "tst_test.h"

#define FILE_A "tmpfileA"
#define FILE_B "tmpfileB"

static int fd1, fd2;

static void my_test(void)
{
	TEST((fd2 = open(FILE_B, O_RDWR | O_CREAT, 0777)));

	if (TEST_RETURN != -1)
		tst_res(TFAIL, "call succeeded unexpectedly");

	if (TEST_ERRNO != EMFILE)
		tst_res(TFAIL, "Expected EMFILE, got %d", TEST_ERRNO);
	else
		tst_res(TPASS, "call returned expected EMFILE error");
}

static void setup(void)
{
	struct rlimit rlim, r;

	fd1 = SAFE_OPEN(FILE_A, O_RDWR | O_CREAT, 0777);
	SAFE_GETRLIMIT(RLIMIT_NOFILE, &rlim);
	r.rlim_cur = fd1;
	r.rlim_max = rlim.rlim_max;
	SAFE_SETRLIMIT(RLIMIT_NOFILE, &r);
}

static void cleanup(void)
{
	if (fd1 > 0)
		SAFE_CLOSE(fd1);
	if (fd2 > 0)
		SAFE_CLOSE(fd2);
}

static struct tst_test test = {
	.test_all = my_test,
	.setup = setup,
	.cleanup = cleanup,
	.needs_tmpdir = 1
};
