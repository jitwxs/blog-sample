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
 *    TEST IDENTIFIER   : exit05
 *
 *    TEST TITLE        : Basic tests for _exit(2)
 *
 *    TEST CASE TOTAL   : 1
 *
 *    AUTHOR            : jitwxs
 *						<jitwxs@foxmail.com>
 *
 *    DESCRIPTION
 *      When process call _exit () terminates, atexit (3) or on_exit (3) is
 *		not called as it did for exit (3), so you can test whether the 
 *		function registered in atexit (3) is called back.
 *
 **********************************************************/

#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <signal.h>
#include "tst_test.h"
#include <stdlib.h>

#define TMPFILE "tmpFile"

void check_call(void)
{
	SAFE_TOUCH(TMPFILE, 0777, NULL);
}

static void my_test(void)
{
	if (!SAFE_FORK()) {
		atexit(check_call);
		_exit(0);
	} else {
		SAFE_WAIT(NULL);
		if (access(TMPFILE, F_OK) != 0)
			tst_res(TPASS, "_exit() Success!");
		else
			tst_res(TFAIL|TERRNO, "_exit() Failed!");
	}
}

static struct tst_test test = {
	.needs_tmpdir = 1,
	.test_all = my_test,
	.forks_child = 1
};
