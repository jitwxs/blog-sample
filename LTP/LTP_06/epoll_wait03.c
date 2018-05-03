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
 *    TEST IDENTIFIER   : epoll_wait03
 *
 *    TEST TITLE        : Basic tests for epoll_wait(2)
 *
 *    TEST CASE TOTAL   : 5
 *
 *    AUTHOR            : jitwxs
 *						<jitwxs@foxmail.com>
 *
 *    DESCRIPTION
 *		1) epoll_wait(2) fails if epfd is not a valid file descriptor
 *		2) epoll_wait(2) fails if epfd is not an epoll file descriptor
 *		3) epoll_wait(2) fails if maxevents is less than zero
 *		4) epoll_wait(2) fails if maxevents is equal to zero
 *		5) epoll_wait(2) fails if the memory area pointed to by events
 *			is not accessible with write permissions.
 *
 *    EXPECTED RESULT
 *		1) epoll_wait(2) should return -1 and set errno to EBADF
 *		2) epoll_wait(2) should return -1 and set errno to EINVAL
 *		3) epoll_wait(2) should return -1 and set errno to EINVAL
 *		4) epoll_wait(2) should return -1 and set errno to EINVAL
 *		5) epoll_wait(2) should return -1 and set errno to EFAULT
 *
 **********************************************************/

#include <sys/epoll.h>
#include <sys/mman.h>
#include <unistd.h>
#include <string.h>
#include <errno.h>

#include "tst_test.h"

static int page_size, fds[2], epfd, inv_epfd, bad_epfd = -1;

static struct epoll_event epevs[1] = {
	{.events = EPOLLOUT}
};

static struct epoll_event *ev_rdwr = epevs;
static struct epoll_event *ev_rdonly;

static struct tcase {
	int *epfd;
	struct epoll_event **ev;
	int maxevents;
	int exp_errno;
} tcases[] = {
	{&bad_epfd, &ev_rdwr, 1, EBADF},
	{&inv_epfd, &ev_rdwr, 1,  EINVAL},
	{&epfd, &ev_rdwr, -1,  EINVAL},
	{&epfd, &ev_rdwr, 0,  EINVAL},
	{&epfd, &ev_rdonly, 1,  EFAULT}
};

static void my_test(unsigned int n)
{
	struct tcase *tc = &tcases[n];

	TEST(epoll_wait(*(tc->epfd), *(tc->ev), tc->maxevents, -1));

	if (TEST_RETURN != -1)
		tst_res(TFAIL, "epoll_wait() succeed unexpectedly");
	else
		if (tc->exp_errno == TEST_ERRNO)
			tst_res(TPASS | TTERRNO,
					"epoll_wait() fails as expected");
		else
			tst_res(TFAIL | TTERRNO,
					"epoll_wait() fails unexpectedly, expected %d: %s",
					tc->exp_errno,
					strerror(tc->exp_errno));
}

static void setup(void)
{
	page_size = getpagesize();

	ev_rdonly = SAFE_MMAP(NULL, page_size, PROT_READ,
			MAP_PRIVATE | MAP_ANONYMOUS, -1, 0);

	SAFE_PIPE(fds);

	epfd = epoll_create(1);
	if (epfd == -1)
		tst_brk(TBROK | TERRNO, "failed to create epoll instance");

	epevs[0].data.fd = fds[1];

	if (epoll_ctl(epfd, EPOLL_CTL_ADD, fds[1], &epevs[0]))
		tst_brk(TBROK | TERRNO, "failed to register epoll target");
}

static void cleanup(void)
{
	if (epfd > 0 && close(epfd))
		tst_res(TWARN | TERRNO, "failed to close epfd");

	if (close(fds[0]))
		tst_res(TWARN | TERRNO, "close(fds[0]) failed");

	if (close(fds[1]))
		tst_res(TWARN | TERRNO, "close(fds[1]) failed");
}

static struct tst_test test = {
	.tcnt = ARRAY_SIZE(tcases),
	.test = my_test,
	.needs_checkpoints = 1,
	.setup = setup,
	.cleanup = cleanup
};
