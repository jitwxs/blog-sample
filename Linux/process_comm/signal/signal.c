#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>

void my_func(int sig_no) {
	if (sig_no == SIGINT)
		printf("Catch SIGINT...\n");
	else if (sig_no == SIGQUIT)
		printf("Catch SIGQUIT...\n");
}

int main(void) {
    printf("waiting for signal SIGINT or SIGQUIT\n");
	signal(SIGINT, my_func);
	signal(SIGQUIT, my_func);
	pause();
    return 0;
}
