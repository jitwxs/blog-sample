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
 *    TEST IDENTIFIER   : mmap05
 *
 *    TEST TITLE        : Basic tests for mmap(2)
 *
 *    TEST CASE TOTAL   : 1
 *
 *    AUTHOR            : jitwxs
 *						  <jitwxs@foxmail.com>
 *
 *    DESCRIPTION
 *		Call mmap() to map a file creating mapped memory with no access
 *		under the following conditions -
 *		- The prot parameter is set to PROT_NONE
 *		- The file descriptor is open for read(any mode other than write)
 *		- The minimum file permissions should be 0444.
 *
 *		The call should succeed to map the file creating mapped memory
 *		with the required attributes.
 *
 *    EXPECTED RESULT
 *		mmap() should succeed returning the address of the mapped region,
 *		and an attempt to access the contents of the mapped region
 *		should give rise to the signal SIGSEGV.
 *
 ********************************************************/

#include <stdlib.h>
#include <errno.h>
#include <setjmp.h>
#include "tst_test.h"
#include <signal.h>

#define TEMPFILE "tempFile"

static size_t page_sz;
static volatile char *addr;
static volatile int pass = 0;
static int fd;
static sigjmp_buf env;

static void sig_handler(int sig)
{
	if (sig == SIGSEGV) {
		pass = 1;
		siglongjmp(env, 1);
	} else
		tst_brk(TBROK, "received an unexpected signal: %d", sig);
}

static void setup(void)
{
	char *buf;

	signal(SIGSEGV, sig_handler);

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

	if ((size_t)write(fd, buf, page_sz) != page_sz) {
		free(buf);
		tst_brk(TFAIL, "writing to %s failed", TEMPFILE);
	}
	free(buf);

	if (fchmod(fd, 0444) < 0)
		tst_brk(TFAIL | TERRNO, "fchmod of %s failed", TEMPFILE);

	if (close(fd) < 0)
		tst_brk(TFAIL | TERRNO, "closing %s failed", TEMPFILE);

	fd = open(TEMPFILE, O_RDONLY);
	if (fd < 0)
		tst_brk(TFAIL | TERRNO,
				"opening %s read-only failed", TEMPFILE);
}

static void cleanup(void)
{
	close(fd);
}

static void do_test(void)
{
	char file_content;

	addr = mmap(0, page_sz, PROT_NONE,
			MAP_FILE | MAP_SHARED, fd, 0);
	TEST_ERRNO = errno;

	if (addr == MAP_FAILED)
		tst_res(TFAIL | TERRNO, "mmap of %s failed", TEMPFILE);

	if (sigsetjmp(env, 1) == 0)
		file_content = addr[0];

	if (pass)
		tst_res(TPASS, "Got SIGSEGV as expected");
	else
		tst_res(TFAIL,
				"Mapped memory region with NO access is accessible");

	if (munmap((void *)addr, page_sz) != 0)
		tst_brk(TFAIL, "munmapping failed");

	pass = 0;
	exit(EXIT_SUCCESS);
}

static struct tst_test test = {
	.test_all = do_test,
	.setup = setup,
	.cleanup = cleanup,
	.needs_tmpdir = 1
};
