package com.brunomdleal.portfolio.qa.heimdall.cli;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PomVersionProviderTest {

    @Test
    void getHeimdallVersion_Success() throws Exception {
        PomVersionProvider pomVersionProvider = new PomVersionProvider();
        assertTrue(pomVersionProvider.getVersion().length > 0);
        assertTrue(pomVersionProvider.getVersion()[0].contains("1.0.0"));
    }

    @Test
    void getHeimdallVersion_WhenResourceMissing_ReturnsUnknown() throws Exception {
        PomVersionProvider pomVersionProvider = new PomVersionProvider("/non-existent.properties");
        assertEquals("Heimdall ATF Gatekeeper unknown", pomVersionProvider.getVersion()[0]);
    }
}
