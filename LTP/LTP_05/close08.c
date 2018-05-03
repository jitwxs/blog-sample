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
 *    TEST IDENTIFIER   : close08
 *
 *    TEST TITLE        : Basic tests for close(2)
 *
 *    TEST CASE TOTAL   : 1
 *
 *    AUTHOR            : jitwxs
 *						  <jitwxs@foxmail.com>
 *
 *    DESCRIPTION
 *      It should/will be extended when full functional tests are written
 *		for close(2).
 *
 **********************************************************/

#include <errno.h>
#include "tst_test.h"

#define FILENAME "tmpFile"

int fd;

static void my_test(void)
{
	TEST(close(fd));

	if (TEST_RETURN == -1)
		tst_res(TFAIL | TTERRNO,
				"close(%s) failed", FILENAME);
	else
		tst_res(TPASS, "close(%s) returned %ld",
				FILENAME, TEST_RETURN);
}

static void setup(void)
{
	fd = SAFE_OPEN(FILENAME, O_RDWR | O_CREAT, 0700);
	if (fd == -1)
		tst_res(TBROK | TTERRNO,
				"open(%s, O_RDWR|O_CREAT,0700) failed",
				FILENAME);
}

static struct tst_test test = {
	.test_all = my_test,
	.setup = setup,
	.needs_tmpdir = 1
};
