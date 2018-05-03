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
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 */
/**********************************************************
 *
 *    TEST IDENTIFIER   : getpagesize01
 *
 *    TEST TITLE        : Basic tests for getpagesize(2)
 *
 *    TEST CASE TOTAL   : 1
 *
 *    AUTHOR            : jitwxs
 *						  <jitwxs@foxmail.com>
 *
 *    DESCRIPTION
 *      This is a Phase I test for the getpagesize(2) system call.
 *      It is intended to provide a limited exposure of the system call.
 *
 **********************************************************/

#include <errno.h>
#include "tst_test.h"

static void testGetpagesize(void)
{
	int size, ret_sysconf;

	TEST(getpagesize());

	if (TEST_RETURN == -1) {
		tst_res(TFAIL | TTERRNO, "getpagesize failed");
	} else {
		size = getpagesize();
		tst_res(TINFO, "Page Size is %d", size);
		ret_sysconf = sysconf(_SC_PAGESIZE);
		if (size == ret_sysconf)
			tst_res(TPASS,
					"getpagesize - Page size returned %d",
					ret_sysconf);
		else
			tst_res(TFAIL,
					"getpagesize - Page size returned %d",
					ret_sysconf);
	}
}

static struct tst_test test = {
	.test_all = testGetpagesize
};
