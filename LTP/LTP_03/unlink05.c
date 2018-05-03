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
 *    TEST IDENTIFIER   : unlink05
 *
 *    TEST TITLE        : Basic tests for unlink(1)
 *
 *    TEST CASE TOTAL   : 1
 *
 *    AUTHOR            : jitwxs
 *						  <jitwxs@foxmail.com>
 *
 *    DESCRIPTION
 *      It should/will be extended when full functional tests are written for
 *		unlink(2).
 *
 **********************************************************/

#include <errno.h>
#include "tst_test.h"

#define TMPFILE "tmpfile"

static void testUnlink(void)
{
	TEST(unlink(TMPFILE));

	if (TEST_RETURN == -1 || access(TMPFILE, F_OK) == 0)
		tst_res(TFAIL | TTERRNO, "unlink(%s) Failed, errno=%d : %s",
				TMPFILE, TEST_ERRNO, strerror(TEST_ERRNO));
	else
		tst_res(TPASS, "unlink(%s) returned %ld",
				TMPFILE, TEST_RETURN);
}

static void setup(void)
{
	SAFE_TOUCH(TMPFILE, 0777, NULL);
}

static struct tst_test test = {
	.needs_tmpdir = 1,
	.setup = setup,
	.test_all = testUnlink
};