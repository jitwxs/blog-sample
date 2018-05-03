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
 *    TEST IDENTIFIER   : getppid02
 *
 *    TEST TITLE        : Basic tests for getppid(2)
 *
 *    TEST CASE TOTAL   : 1
 *
 *    AUTHOR            : jitwxs
 *						  <jitwxs@foxmail.com>
 *
 *    DESCRIPTION
 *      Testcase to check the basic functionality of the getppid() syscall.
 *
 **********************************************************/

#include <errno.h>
#include <sys/wait.h>
#include "tst_test.h"

static void testGetppid(void)
{
	int status;
	pid_t pid, ppid;

	ppid = getpid();
	pid = SAFE_FORK();

	if (pid == -1)
		tst_res(TFAIL, "fork failed");
	else if (pid == 0) {
		TEST(getppid());
		if (ppid != TEST_RETURN)
			tst_res(TFAIL | TERRNO,
					"getppid Failed (%ld != %d)",
					TEST_RETURN, ppid);
		else
			tst_res(TPASS,
					"getppid Success (%ld == %d)",
					TEST_RETURN, ppid);
	} else {
		if (wait(&status) == -1)
			tst_res(TBROK | TERRNO,
					"wait failed");
		if (!WIFEXITED(status) || WEXITSTATUS(status) != 0)
			tst_res(TFAIL,
					"getppid functionality incorrect");
	}
}

static struct tst_test test = {
	.test_all = testGetppid,
	.forks_child = 1
};
