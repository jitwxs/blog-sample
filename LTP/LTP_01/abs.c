#include <errno.h>
#include <string.h>
#include <sys/mount.h>
#include "tst_test.h"

static struct tcase {
	const int input;
	const int output;
} tcases[] = {
        {-1,1},
        {3,3}
};

static void testAbs(unsigned int n)
{
        struct tcase *tc = &tcases[n];

        TEST(abs(tc->input));
       
	if (TEST_RETURN != tc->output) {
                tst_res(TFAIL, "abs() failed");
                return;
        }
	tst_res(TPASS, "abs() succeeds");
}

static struct tst_test test = {
        .tid = "testAbs",
        .tcnt = ARRAY_SIZE(tcases),
        .test = testAbs,
};
