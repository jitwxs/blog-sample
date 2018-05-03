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
 *    TEST IDENTIFIER   : mmap04
 *
 *    TEST TITLE        : Basic tests for mmap(2)
 *
 *    TEST CASE TOTAL   : 1
 *
 *    AUTHOR            : jitwxs
 *						  <jitwxs@foxmail.com>
 *
 *    DESCRIPTION
 *		Call mmap() to map a file creating a mapped region with read/exec access
 *		under the following conditions -
 *		- The prot parameter is set to PROT_READ|PROT_EXEC
 *		- The file descriptor is open for read
 *		- The file being mapped has read and execute permission bit set.
 *		- The minimum file permissions should be 0555.
 *
 *		The call should succeed to map the file creating mapped memory with the
 *		required attributes.
 *
 *    EXPECTED RESULT
 *		mmap() should succeed returning the address of the mapped region,and the
 *		mapped region should contain the contents of the mapped file.
 *
 ********************************************************/

#include <stdlib.h>
#include <errno.h>
#include "tst_test.h"

#define TEMPFILE "tempFile"

static size_t page_sz;
static char *addr;
static char *dummy;
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

	if (fchmod(fd, 0555) < 0)
		tst_brk(TFAIL, "fchmod of %s failed", TEMPFILE);

	if (close(fd) < 0)
		tst_brk(TFAIL, "closing %s failed", TEMPFILE);

	dummy = calloc(page_sz, sizeof(char));
	if (dummy == NULL)
		tst_brk(TFAIL, "calloc failed (dummy)");

	fd = open(TEMPFILE, O_RDONLY);
	if (fd < 0)
		tst_brk(TFAIL, "opening %s read-only failed", TEMPFILE);
}

static void cleanup(void)
{
	close(fd);
	free(dummy);
}

static void do_test(void)
{
	addr = mmap(0, page_sz, PROT_READ | PROT_EXEC,
				MAP_FILE | MAP_SHARED, fd, 0);
	TEST_ERRNO = errno;

	if (addr == MAP_FAILED)
		tst_res(TFAIL | TERRNO, "mmap of %s failed", TEMPFILE);

	if ((read(fd, dummy, page_sz)) < 0)
		tst_brk(TFAIL, "reading %s failed", TEMPFILE);

	if ((memcmp(dummy, addr, page_sz)))
		tst_res(TFAIL,
				"mapped memory region contains invalid data");
	else
		tst_res(TPASS,
				"Functionality of mmap() successful");

	if ((munmap(addr, page_sz)) != 0)
		tst_brk(TFAIL, "munmapping failed");

	cleanup();
	exit(EXIT_SUCCESS);
}

static struct tst_test test = {
	.test_all = do_test,
	.setup = setup,
	.cleanup = cleanup,
	.needs_tmpdir = 1
};
