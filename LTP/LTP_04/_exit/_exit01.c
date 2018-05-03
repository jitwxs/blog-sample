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
 *    TEST IDENTIFIER   : exit01
 *
 *    TEST TITLE        : Basic tests for _exit(2)
 *
 *    TEST CASE TOTAL   : 1
 *
 *    AUTHOR            : jitwxs
 *						<jitwxs@foxmail.com>
 *
 *    DESCRIPTION
 *		After the process calls _exit() to test whether the process still
 *		exists.
 *
 **********************************************************/

#include <errno.h>
#include <unistd.h>
#include <sys/types.h>
#include <signal.h>
#include "tst_test.h"

static void my_test(void)
{
	pid_t pid;
	int tmp;

	pid = SAFE_FORK();
	if (!pid) {
		_exit(0);
	} else {
		SAFE_WAITPID(pid, NULL, 0);
		tmp = kill(pid, 0);
		if (tmp != -1)
			tst_res(TFAIL | TERRNO, "_exit() Failed!");
		else
			tst_res(TPASS, "_exit() Success!");
	}
}

static struct tst_test test = {
	.test_all = my_test,
	.forks_child = 1
};
