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
 *    TEST IDENTIFIER   : exit02
 *
 *    TEST TITLE        : Basic tests for _exit(2)
 *
 *    TEST CASE TOTAL   : 1
 *
 *    AUTHOR            : jitwxs
 *						<jitwxs@foxmail.com>
 *
 *    DESCRIPTION
 *		After the process call _exit () terminates,
 *		tests if the child process's parent process PID is 1.
 *
 **********************************************************/

#include <unistd.h>
#include <sys/types.h>
#include <signal.h>
#include "tst_test.h"

static void my_test(void)
{
	pid_t pid1, pid2;

	pid1 = SAFE_FORK();

	if (pid1 == 0) {
		pid2 = SAFE_FORK();
		if (pid2 != 0)
			_exit(0);
		else {
			usleep(10);
			if (getppid() == 1)
				tst_res(TPASS, "_exit() Success!");
			else
				tst_res(TFAIL | TERRNO, "_exit() Failed!");
			TST_CHECKPOINT_WAKE(0);
			_exit(0);
		}
	} else {
		TST_CHECKPOINT_WAIT(0);
	}
}

static struct tst_test test = {
	.test_all = my_test,
	.forks_child = 1,
	.needs_checkpoints = 1
};
