CFLAGS = -g -Wall 
LDFLAGS = -lpthread

src = $(wildcard *.c)
target = $(patsubst %.c, %, ${src})  
 
.PHONY: all clean

%.o:%.c
	gcc ${CFLAGS} -c -o $@  

all: ${target}  

clean:  
	rm -f ${target}                                                     
