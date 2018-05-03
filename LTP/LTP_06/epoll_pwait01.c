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
 *    TEST IDENTIFIER   : epoll_pwait01
 *
 *    TEST TITLE        : Basic tests for epoll_wait(2)
 *
 *    TEST CASE TOTAL   : 2
 *
 *    AUTHOR            : jitwxs
 *						<jitwxs@foxmail.com>
 *
 *    DESCRIPTION
 *		Basic test for epoll_pwait(2).
 *		1)  epoll_pwait(2) with sigmask argument allows the caller to
 *			safely wait until either a file descriptor becomes ready
 *			or the timeout expires.
 *		2)  epoll_pwait(2) with NULL sigmask argument fails if
 *			interrupted by a signal handler, epoll_pwait(2) should
 *			return -1 and set errno to EINTR.include <sys/epoll.h>
 *
 **********************************************************/

#include <sys/epoll.h>
#include <sys/types.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>

#include "tst_test.h"

static int epfd, fds[2];
static struct sigaction sa;
static struct epoll_event epevs;
static sigset_t signalset;

static struct tcase {
	sigset_t *sig;
	int ret_val;
	int exp_errno;
} tcases[] = {
	{&signalset, 0, 0},
	{NULL, -1, EINTR}
};

static void sighandler(int sig LTP_ATTRIBUTE_UNUSED)
{
}

static void setup(void)
{
	if (sigemptyset(&signalset) == -1)
		tst_brk(TFAIL | TERRNO, "sigemptyset() failed");

	if (sigaddset(&signalset, SIGUSR1) == -1)
		tst_brk(TFAIL | TERRNO, "sigaddset() failed");

	sa.sa_flags = 0;
	sa.sa_handler = sighandler;
	if (sigemptyset(&sa.sa_mask) == -1)
		tst_brk(TFAIL | TERRNO, "sigemptyset() failed");

	if (sigaction(SIGUSR1, &sa, NULL) == -1)
		tst_brk(TFAIL | TERRNO, "sigaction() failed");

	SAFE_PIPE(fds);

	epfd = epoll_create(1);
	if (epfd == -1)
		tst_brk(TBROK | TERRNO, "failed to create epoll instance");

	epevs.events = EPOLLIN;
	epevs.data.fd = fds[0];

	if (epoll_ctl(epfd, EPOLL_CTL_ADD, fds[0], &epevs) == -1)
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

static void my_test(unsigned int n)
{
	struct tcase *tc = &tcases[n];

	if (SAFE_FORK() == 0) {
		TST_PROCESS_STATE_WAIT(getppid(), 'S');
		SAFE_KILL(getppid(), SIGUSR1);

		cleanup();
		exit(EXIT_SUCCESS);
	}

	TEST(epoll_pwait(epfd, &epevs, 1, 100, tc->sig));

	if (tc->ret_val == TEST_RETURN)
		if (TEST_RETURN == 0 || tc->exp_errno == TEST_ERRNO) {
			tst_res(TPASS, "epoll_pwait() pass");
			return;
		}

	tst_res(TFAIL | TTERRNO, "epoll_pwait() failed");
}

static struct tst_test test = {
	.tcnt = ARRAY_SIZE(tcases),
	.test = my_test,
	.needs_checkpoints = 1,
	.forks_child = 1,
	.setup = setup,
	.cleanup = cleanup,
	.min_kver = "2.6.19"
};
