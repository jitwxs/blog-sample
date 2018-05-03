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
 *    TEST IDENTIFIER   : exit04
 *
 *    TEST TITLE        : Basic tests for _exit(2)
 *
 *    TEST CASE TOTAL   : 5
 *
 *    AUTHOR            : jitwxs
 *						<jitwxs@foxmail.com>
 *
 *    DESCRIPTION
 *      The process calls _exit () to append the parameter value and test
 *		whether the value was obtained from the parent's wait ().
 *
 **********************************************************/

#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <signal.h>
#include "tst_test.h"

static struct tcase {
	const int input;
	const int output;
} tcases[] = {
	{0, 0},
	{10, 10},
	{128, 128},
	{256, 0},
	{288, 32}
};

static void my_test(unsigned int n)
{
	struct tcase *tc = &tcases[n];
	pid_t pid;
	int status;

	if (!SAFE_FORK()) {
		pid = SAFE_FORK();
		if (pid == 0)
			_exit(tc->input);
		else {
			SAFE_WAITPID(pid, &status, 0);
			if (WEXITSTATUS(status) != tc->output)
				tst_res(TFAIL | TERRNO, "_exit() Failed!");
			else
				tst_res(TPASS, "_exit() Success!");
			TST_CHECKPOINT_WAKE(0);
			_exit(0);
		}
	} else {
		TST_CHECKPOINT_WAIT(0);
	}
}

static struct tst_test test = {
	.tcnt = ARRAY_SIZE(tcases),
	.test = my_test,
	.forks_child = 1,
	.needs_checkpoints = 1
};
