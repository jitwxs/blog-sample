package com.github.jitwxs.sample.aeron.agrona;

import org.agrona.PropertyAction;
import org.agrona.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @see org.agrona.SystemUtil
 */
public class SystemUtilTest {
    @Test
    public void shouldParseSizesWithSuffix() {
        assertEquals(1L, SystemUtil.parseSize("", "1"));
        assertEquals(1024L, SystemUtil.parseSize("", "1k"));
        assertEquals(1024L, SystemUtil.parseSize("", "1K"));
        assertEquals(1024L * 1024L, SystemUtil.parseSize("", "1m"));
        assertEquals(1024L * 1024L, SystemUtil.parseSize("", "1M"));
        assertEquals(1024L * 1024L * 1024L, SystemUtil.parseSize("", "1g"));
        assertEquals(1024L * 1024L * 1024L, SystemUtil.parseSize("", "1G"));
    }

    @Test
    public void shouldParseTimesWithSuffix() {
        assertEquals(1L, SystemUtil.parseDuration("", "1"));
        assertEquals(1L, SystemUtil.parseDuration("", "1ns"));
        assertEquals(1L, SystemUtil.parseDuration("", "1NS"));
        assertEquals(1000L, SystemUtil.parseDuration("", "1us"));
        assertEquals(1000L, SystemUtil.parseDuration("", "1US"));
        assertEquals(1000L * 1000, SystemUtil.parseDuration("", "1ms"));
        assertEquals(1000L * 1000, SystemUtil.parseDuration("", "1MS"));
        assertEquals(1000L * 1000 * 1000, SystemUtil.parseDuration("", "1s"));
        assertEquals(1000L * 1000 * 1000, SystemUtil.parseDuration("", "1S"));
        assertEquals(12L * 1000 * 1000 * 1000, SystemUtil.parseDuration("", "12S"));
    }

    @Test
    public void shouldThrowWhenParseTimeHasBadSuffix() {
        assertThrows(NumberFormatException.class, () -> SystemUtil.parseDuration("", "1g"));
    }

    @Test
    public void shouldThrowWhenParseTimeHasBadTwoLetterSuffix() {
        assertThrows(NumberFormatException.class, () -> SystemUtil.parseDuration("", "1zs"));
    }

    @Test
    public void shouldThrowWhenParseSizeOverflows() {
        assertThrows(NumberFormatException.class, () -> SystemUtil.parseSize("", 8589934592L + "g"));
    }

    @Test
    public void shouldDoNothingToSystemPropsWhenLoadingFileWhichDoesNotExist() {
        final int originalSystemPropSize = System.getProperties().size();

        SystemUtil.loadPropertiesFile("$unknown-file$");
        assertEquals(originalSystemPropSize, System.getProperties().size());
    }

    @Test
    public void shouldMergeMultiplePropFilesTogether() {
        assertTrue(StringUtils.isEmpty(System.getProperty("TestFileA.foo")));
        assertTrue(StringUtils.isEmpty(System.getProperty("TestFileB.foo")));

        try {
            SystemUtil.loadPropertiesFiles("TestFileA.properties", "TestFileB.properties");
            assertEquals("AAA", System.getProperty("TestFileA.foo"));
            assertEquals("BBB", System.getProperty("TestFileB.foo"));
        } finally {
            System.clearProperty("TestFileA.foo");
            System.clearProperty("TestFileB.foo");
        }
    }

    @Test
    public void shouldOverrideSystemPropertiesWithConfigFromPropFile() {
        System.setProperty("TestFileA.foo", "ToBeOverridden");
        assertEquals("ToBeOverridden", System.getProperty("TestFileA.foo"));

        try {
            SystemUtil.loadPropertiesFiles("TestFileA.properties");
            assertEquals("AAA", System.getProperty("TestFileA.foo"));
        } finally {
            System.clearProperty("TestFileA.foo");
        }
    }

    @Test
    public void shouldNotOverrideSystemPropertiesWithConfigFromPropFile() {
        System.setProperty("TestFileA.foo", "ToBeNotOverridden");
        assertEquals("ToBeNotOverridden", System.getProperty("TestFileA.foo"));

        try {
            SystemUtil.loadPropertiesFile(PropertyAction.PRESERVE, "TestFileA.properties");
            assertEquals("ToBeNotOverridden", System.getProperty("TestFileA.foo"));
        } finally {
            System.clearProperty("TestFileA.foo");
        }
    }

    @Test
    public void shouldReturnPid() {
        assertNotEquals(SystemUtil.PID_NOT_FOUND, SystemUtil.getPid());
    }

    @Test
    public void shouldGetNormalProperty() {
        final String key = "org.agrona.test.case";
        final String value = "wibble";

        System.setProperty(key, value);

        try {
            assertEquals(value, SystemUtil.getProperty(key));
        } finally {
            System.clearProperty(key);
        }
    }

    @Test
    public void shouldGetNullProperty() {
        final String key = "org.agrona.test.case";
        final String value = "@null";

        System.setProperty(key, value);

        try {
            assertNull(SystemUtil.getProperty(key));
        } finally {
            System.clearProperty(key);
        }
    }

    @Test
    public void shouldGetNullPropertyWithDefault() {
        final String key = "org.agrona.test.case";
        final String value = "@null";

        System.setProperty(key, value);

        try {
            assertNull(SystemUtil.getProperty(key, "default"));
        } finally {
            System.clearProperty(key);
        }
    }

    @Test
    public void shouldGetDefaultProperty() {
        final String key = "org.agrona.test.case";
        final String defaultValue = "default";

        assertEquals(defaultValue, SystemUtil.getProperty(key, defaultValue));
    }
}
