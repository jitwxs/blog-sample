package com.github.jitwxs.sample.nio;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author jitwxs
 * @date 2021-08-29 20:34
 */
public class FileTest {
    /**
     * 文件读取
     * <p>
     * 没考虑编码，因此文件只能用英文，不能用汉字；
     * <p>
     * 没有判断读取大小，写死的 512，因此文件数据不能太多。
     */
    @Test
    public void testRead() throws Exception {
        final FileInputStream inputStream = new FileInputStream("FileTest.txt");
        final FileChannel channel = inputStream.getChannel();

        final ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        channel.read(byteBuffer);

        byteBuffer.flip();

        while (byteBuffer.hasRemaining()) {
            final byte b = byteBuffer.get();
            System.out.println((char) b);
        }

        inputStream.close();
    }

    /**
     * 文件写入
     */
    @Test
    public void testWrite() throws Exception {
        final FileOutputStream outputStream = new FileOutputStream("FileTest.txt");
        final FileChannel channel = outputStream.getChannel();

        final byte[] messages = "Hello world FileTest.txt".getBytes(StandardCharsets.UTF_8);

        final ByteBuffer byteBuffer = ByteBuffer.allocate(messages.length);
        byteBuffer.put(messages);

        byteBuffer.flip();

        channel.write(byteBuffer);

        outputStream.close();
    }

    /**
     * 读取文件内容到另一文件中
     */
    @Test
    public void testReadAndWrite() throws Exception {
        final FileInputStream inputStream = new FileInputStream("FileTest.txt");
        final FileOutputStream outputStream = new FileOutputStream("FileTestOutput.txt");

        final FileChannel inputChannel = inputStream.getChannel();
        final FileChannel outputChannel = outputStream.getChannel();

        final ByteBuffer byteBuffer = ByteBuffer.allocate(512);

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
