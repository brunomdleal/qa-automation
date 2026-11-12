package com.brunomdleal.portfolio.qa.heimdall.cli;

import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RunTestsCommandTest {

    @Test
    void executeCommand_WithDefaultOptions_Success() {
        int exitCode = new CommandLine(new RunTestsCommand()).execute();
        assertEquals(0, exitCode);
    }

    @Test
    void executeCommand_WithCustomArgs_Success() {
        int exitCode = new CommandLine(new RunTestsCommand())
                .execute("-e", "dev", "-t", "api,db,ui", "-u", "http://dev.order.local", "--parallel");
        assertEquals(0, exitCode);
    }
}