package com.github.jitwxs.sample.aeron.agrona.buffer;

import org.agrona.ExpandableArrayBuffer;
import org.agrona.PrintBufferUtil;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @see PrintBufferUtil
 */
public class PrintBufferUtilTest {
    @Test
    public void shouldPrettyPrintHex() {
        final String contents = "Hello World!\nThis is a test String\nto print out.";
        final ExpandableArrayBuffer buffer = new ExpandableArrayBuffer();

        buffer.putStringAscii(0, contents);

        final StringBuilder builder = new StringBuilder();
        PrintBufferUtil.appendPrettyHexDump(builder, buffer);
        assertThat(builder.toString(), containsString("0...Hello World!"));
    }
}
