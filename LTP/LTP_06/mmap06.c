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
 *    TEST IDENTIFIER   : mmap06
 *
 *    TEST TITLE        : Basic tests for mmap(2)
 *
 *    TEST CASE TOTAL   : 1
 *
 *    AUTHOR            : jitwxs
 *						  <jitwxs@foxmail.com>
 *
 *    DESCRIPTION
 *		Call mmap() to map a file creating a mapped region with read
 *		access under the following conditions -
 *		- The prot parameter is set to PROT_READ
 *		- The file descriptor is open for writing.
 *
 *		The call should fail to map the file.
 *
 *    EXPECTED RESULT
 *		mmap() should fail returning -1 and errno should get set to
 *		EACCES.
 *
 **********************************************************/

#include <stdlib.h>
#include <errno.h>
#include "tst_test.h"

#define TEMPFILE "tempFile"

static size_t page_sz;
static char *addr;
static int fd;

static void setup(void)
{
	char *buf;

	page_sz = getpagesize();

	buf = calloc(page_sz, sizeof(char));
	if (buf == NULL)
		tst_brk(TFAIL, "calloc() failed (tst_buff)");

	memset(buf, 'A', page_sz);

	fd = open(TEMPFILE, O_WRONLY | O_CREAT, 0666);
	if (fd < 0) {
		free(buf);
		tst_brk(TFAIL, "opening %s failed", TEMPFILE);
	}

	if ((size_t)write(fd, buf, page_sz) < page_sz) {
		free(buf);
		tst_brk(TFAIL, "writing to %s failed", TEMPFILE);
	}
	free(buf);
}

static void cleanup(void)
{
	close(fd);
}

static void do_test(void)
{
	addr = mmap(0, page_sz, PROT_READ,
				MAP_FILE | MAP_SHARED, fd, 0);
	TEST_ERRNO = errno;

	if (addr != MAP_FAILED) {
		tst_res(TFAIL | TERRNO,
				"mmap() returned invalid value, expected: %p",
				MAP_FAILED);
		if (munmap(addr, page_sz) != 0) {
			tst_res(TBROK, "munmap() failed");
			cleanup();
		}
		exit(EXIT_FAILURE);
	}

	if (TEST_ERRNO == EACCES)
		tst_res(TPASS, "mmap failed with EACCES");
	else
		tst_res(TFAIL | TERRNO,
				"mmap failed with unexpected errno");

	cleanup();
	exit(EXIT_SUCCESS);
}

static struct tst_test test = {
	.test_all = do_test,
	.setup = setup,
	.cleanup = cleanup,
	.needs_tmpdir = 1
};
