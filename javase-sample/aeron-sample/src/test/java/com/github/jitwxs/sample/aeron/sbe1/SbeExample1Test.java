package com.github.jitwxs.sample.aeron.sbe1;

import org.agrona.concurrent.UnsafeBuffer;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SbeExample1Test {
    public static final String TEMPLATE_IDS_DO_NOT_MATCH = "Template ids do not match";
    public static final String MESSAGE_2 = "a message";
    public static final String MESSAGE_1 = "message a";

    @Test
    public void canWriteReadSampleA() {
        final SampleSimpleEncoder encoder = new SampleSimpleEncoder();
        final MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(128);
        final UnsafeBuffer directBuffer = new UnsafeBuffer(byteBuffer);

        encoder.wrapAndApplyHeader(directBuffer, 0, messageHeaderEncoder);
        encoder.sequence(123L);
        encoder.enumField(SampleEnum.VALUE_1);
        encoder.message(MESSAGE_1);

        final SampleSimpleDecoder decoder = new SampleSimpleDecoder();
        final MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
        int bufferOffset = 0;
        headerDecoder.wrap(directBuffer, bufferOffset);

        // Lookup the applicable flyweight to decode this type of message based on templateId and version.
        final int templateId = headerDecoder.templateId();
        if (templateId != SampleSimpleDecoder.TEMPLATE_ID) {
            throw new IllegalStateException(TEMPLATE_IDS_DO_NOT_MATCH);
        }

        final int actingBlockLength = headerDecoder.blockLength();
        final int actingVersion = headerDecoder.version();

        bufferOffset += headerDecoder.encodedLength();
        decoder.wrap(directBuffer, bufferOffset, actingBlockLength, actingVersion);

        assertEquals(123, decoder.sequence());
        assertEquals(SampleEnum.VALUE_1, decoder.enumField());
        assertEquals(MESSAGE_1, decoder.message());
    }

    @Test
    public void canWriteReadSampleRepeatGroups() {
        final SampleGroupEncoder encoder = new SampleGroupEncoder();
        final MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        final UnsafeBuffer directBuffer = new UnsafeBuffer(byteBuffer);

        encoder.wrapAndApplyHeader(directBuffer, 0, messageHeaderEncoder);
        encoder.timestamp(1000L);
        SampleGroupEncoder.GroupEncoder groupEncoder = encoder.groupCount(2);
        groupEncoder.next();
        groupEncoder.groupField1(1);
        groupEncoder.groupField2(2);
        groupEncoder.groupField3(MESSAGE_2);
        groupEncoder.next();
        groupEncoder.groupField1(1);
        groupEncoder.groupField2(2);
        groupEncoder.groupField3(MESSAGE_2);
        encoder.message(MESSAGE_1);


        final SampleGroupDecoder decoder = new SampleGroupDecoder();
        final MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
        int bufferOffset = 0;
        headerDecoder.wrap(directBuffer, bufferOffset);

        // Lookup the applicable flyweight to decode this type of message based on templateId and version.
        final int templateId = headerDecoder.templateId();
        if (templateId != SampleGroupDecoder.TEMPLATE_ID) {
            throw new IllegalStateException(TEMPLATE_IDS_DO_NOT_MATCH);
        }

        final int actingBlockLength = headerDecoder.blockLength();
        final int actingVersion = headerDecoder.version();

        bufferOffset += headerDecoder.encodedLength();
        decoder.wrap(directBuffer, bufferOffset, actingBlockLength, actingVersion);
        assertEquals(1000L, decoder.timestamp());
        for (final SampleGroupDecoder.GroupDecoder groupDecoder : decoder.group()) {
            assertEquals(1, groupDecoder.groupField1());
            assertEquals(2, groupDecoder.groupField2());
            assertEquals(MESSAGE_2, groupDecoder.groupField3());
        }
        assertEquals(MESSAGE_1, decoder.message());
    }

    @Test
    public void dataCanBeCorrupted() {
        final SampleCorruptionEncoder encoder = new SampleCorruptionEncoder();
        final MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(128);
        final UnsafeBuffer directBuffer = new UnsafeBuffer(byteBuffer);

        encoder.wrapAndApplyHeader(directBuffer, 0, messageHeaderEncoder);
        encoder.message2(MESSAGE_2);
        encoder.message1(MESSAGE_1);
        encoder.sequence1(123L);
        encoder.sequence2(321L);

        final SampleCorruptionDecoder decoder = new SampleCorruptionDecoder();
        final MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
        int bufferOffset = 0;
        headerDecoder.wrap(directBuffer, bufferOffset);

        // Lookup the applicable flyweight to decode this type of message based on templateId and version.
        final int templateId = headerDecoder.templateId();
        if (templateId != SampleCorruptionDecoder.TEMPLATE_ID) {
            throw new IllegalStateException(TEMPLATE_IDS_DO_NOT_MATCH);
        }

        final int actingBlockLength = headerDecoder.blockLength();
        final int actingVersion = headerDecoder.version();

        bufferOffset += headerDecoder.encodedLength();
        decoder.wrap(directBuffer, bufferOffset, actingBlockLength, actingVersion);

        assertEquals(123, decoder.sequence1());
        assertNotEquals(MESSAGE_1, decoder.message1());
        assertEquals(321, decoder.sequence2());
        assertNotEquals(MESSAGE_2, decoder.message2());
    }

    @Test
    public void dataCanBeReadCorrectlyWhenWrittenCorrectly() {
        final SampleCorruptionEncoder encoder = new SampleCorruptionEncoder();
        final MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(128);
        final UnsafeBuffer directBuffer = new UnsafeBuffer(byteBuffer);

        encoder.wrapAndApplyHeader(directBuffer, 0, messageHeaderEncoder);
        encoder.sequence1(123L);
        encoder.sequence2(321L);
        encoder.message1(MESSAGE_1);
        encoder.message2(MESSAGE_2);

        final SampleCorruptionDecoder decoder = new SampleCorruptionDecoder();
        final MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
        int bufferOffset = 0;
        headerDecoder.wrap(directBuffer, bufferOffset);

        // Lookup the applicable flyweight to decode this type of message based on templateId and version.
        final int templateId = headerDecoder.templateId();
        if (templateId != SampleCorruptionDecoder.TEMPLATE_ID) {
            throw new IllegalStateException(TEMPLATE_IDS_DO_NOT_MATCH);
        }

        final int actingBlockLength = headerDecoder.blockLength();
        final int actingVersion = headerDecoder.version();

        bufferOffset += headerDecoder.encodedLength();
        decoder.wrap(directBuffer, bufferOffset, actingBlockLength, actingVersion);

        assertEquals(123, decoder.sequence1());
        assertEquals(321, decoder.sequence2());
        assertEquals(MESSAGE_1, decoder.message1());
        assertEquals(MESSAGE_2, decoder.message2());
    }


    @Test
    public void nullStringReturnsAsEmptyString() {
        final SampleCorruptionEncoder encoder = new SampleCorruptionEncoder();
        final MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(128);
        final UnsafeBuffer directBuffer = new UnsafeBuffer(byteBuffer);

        encoder.wrapAndApplyHeader(directBuffer, 0, messageHeaderEncoder);
        encoder.sequence1(123L);
        encoder.sequence2(321L);
        encoder.message1(MESSAGE_1);
        encoder.message2(null);

        final SampleCorruptionDecoder decoder = new SampleCorruptionDecoder();
        final MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
        int bufferOffset = 0;
        headerDecoder.wrap(directBuffer, bufferOffset);

        // Lookup the applicable flyweight to decode this type of message based on templateId and version.
        final int templateId = headerDecoder.templateId();
        if (templateId != SampleCorruptionDecoder.TEMPLATE_ID) {
            throw new IllegalStateException(TEMPLATE_IDS_DO_NOT_MATCH);
        }

        final int actingBlockLength = headerDecoder.blockLength();
        final int actingVersion = headerDecoder.version();

        bufferOffset += headerDecoder.encodedLength();
        decoder.wrap(directBuffer, bufferOffset, actingBlockLength, actingVersion);

        assertEquals(123, decoder.sequence1());
        assertEquals(321, decoder.sequence2());
        assertEquals(MESSAGE_1, decoder.message1());
        assertEquals("", decoder.message2());
    }
}
