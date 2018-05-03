#include <errno.h>
#include <string.h>
#include <sys/mount.h>
#include "tst_test.h"

static struct tcase {
        const int input;
        const int output;
} tcases[] = {
        {-1,1},
        {9,3}
};

static void testSqrt(unsigned int n){
        struct tcase *tc = &tcases[n];

        TEST(sqrt(tc->input));
       
        if (TEST_RETURN != tc->output) {
                tst_res(TFAIL, "sqrt() failed");
                return;
        }
        tst_res(TPASS, "sqrt() succeeds");
}

static struct tst_test test = {
        .tid = "testSqrt",
        .tcnt = ARRAY_SIZE(tcases),
        .test = testSqrt,
};