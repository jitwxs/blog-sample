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
 *    TEST IDENTIFIER   : exit03
 *
 *    TEST TITLE        : Basic tests for _exit(2)
 *
 *    TEST CASE TOTAL   : 1
 *
 *    AUTHOR            : jitwxs
 *						<jitwxs@foxmail.com>
 *
 *    DESCRIPTION
 *      After the process call _exit () terminates,
 *		it tests if the parent process received the SIGCHLD signal.
 *
 **********************************************************/

#include <sys/types.h>
#include <signal.h>
#include "tst_test.h"
#include <stdio.h>

int global_value = 1;

void check_call(int sig)
{
	if (sig == SIGCHLD)
		global_value = 10;
}

static void my_test(void)
{
	pid_t pid;
	int status;

	pid = SAFE_FORK();

	if (pid == 0)
		_exit(0);
	else {
		signal(SIGCHLD, check_call);
		SAFE_WAIT(&status);
		if (global_value == 1)
			tst_res(TFAIL | TERRNO, "_exit() Failed!");
		else if (global_value == 10)
			tst_res(TPASS, "_exit() Success!");
	}
}

static struct tst_test test = {
	.test_all = my_test,
	.forks_child = 1
};
