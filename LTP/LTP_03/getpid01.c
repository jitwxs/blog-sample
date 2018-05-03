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
 *    TEST IDENTIFIER   : getpid01
 *
 *    TEST TITLE        : Basic tests for getpid(2)
 *
 *    TEST CASE TOTAL   : 1
 *
 *    AUTHOR            : jitwxs
 *						  <jitwxs@foxmail.com>
 *
 *    DESCRIPTION
 *      Testcase to check the basic functionality of getpid().
 *
 **********************************************************/

#include <errno.h>
#include "tst_test.h"

static void test_getpid(void)
{
	TEST(getpid());

	if (TEST_RETURN == -1)
		tst_res(TFAIL | TTERRNO, "getpid failed");
	else
		tst_res(TPASS, "getpid returned %ld", TEST_RETURN);
}

static struct tst_test test = {
	.test_all = test_getpid
};
