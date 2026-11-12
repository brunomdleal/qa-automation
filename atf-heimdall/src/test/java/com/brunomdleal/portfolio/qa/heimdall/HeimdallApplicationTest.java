package com.brunomdleal.portfolio.qa.heimdall;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HeimdallApplicationTest {

    @Test
    void runApp_WithDefaultArguments_Success() {
        int exitCode = HeimdallApplication.execute(new String[]{});
        assertEquals(0, exitCode);
    }

    @Test
    void runApp_WithInvalidFlag_Fail() {
        int exitCode = HeimdallApplication.execute(new String[]{"--unknown-flag"});
        assertEquals(2, exitCode);
    }

    @Test
    void main_WithDefaultArguments_ExitsWithZero() {
        IntConsumer originalHandler = HeimdallApplication.exitHandler;
        AtomicInteger capturedCode = new AtomicInteger(-1);
        HeimdallApplication.exitHandler = capturedCode::set;

        try {
            HeimdallApplication.main(new String[]{});
            assertEquals(0, capturedCode.get());
        } finally {
            HeimdallApplication.exitHandler = originalHandler;
        }
    }
}