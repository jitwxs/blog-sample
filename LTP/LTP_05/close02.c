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
 *    TEST IDENTIFIER   : close02
 *
 *    TEST TITLE        : Basic tests for close(2)
 *
 *    TEST CASE TOTAL   : 1
 *
 *    AUTHOR            : jitwxs
 *						  <jitwxs@foxmail.com>
 *
 *    DESCRIPTION
 *      Check that an invalid file descriptor returns EBADF
 *
 **********************************************************/

#include <errno.h>
#include "tst_test.h"

static void my_test(void)
{
	TEST(close(-1));

	if (TEST_RETURN != -1)
		tst_res(TFAIL, "Closed a non existent fildes");
	else {
		if (TEST_ERRNO != EBADF)
			tst_res(TFAIL, "close() FAILED dis EBADF, got %d",
					errno);
		else
			tst_res(TPASS, "call returned EBADF");
	}
}

static struct tst_test test = {
	.test_all = my_test
};
