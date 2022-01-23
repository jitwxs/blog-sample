package com.github.jitwxs.sample.nio;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author jitwxs
 * @date 2021-08-29 20:41
 */
public class DirectBufferTest {
    @Test
    public void testDirect() throws Exception {
        final FileInputStream inputStream = new FileInputStream("FileTest.txt");
        final FileOutputStream outputStream = new FileOutputStream("FileTestOutput.txt");

        final FileChannel inputChannel = inputStream.getChannel();
        final FileChannel outputChannel = outputStream.getChannel();

        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(512);

        while (true) {
            byteBuffer.clear();

            final int readIndex = inputChannel.read(byteBuffer);

            System.out.println("readIndex: " + readIndex);

            if (readIndex == -1) {
                break;
            }

            byteBuffer.flip();

            outputChannel.write(byteBuffer);
        }

        inputStream.close();
        outputStream.close();
    }
}
