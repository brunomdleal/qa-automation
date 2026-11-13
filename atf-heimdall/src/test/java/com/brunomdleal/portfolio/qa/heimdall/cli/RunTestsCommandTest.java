package com.brunomdleal.portfolio.qa.heimdall.cli;

import com.brunomdleal.portfolio.qa.odin.OdinOrchestrator;
import com.brunomdleal.portfolio.qa.odin.model.OdinRequest;
import com.brunomdleal.portfolio.qa.odin.model.OdinResponse;
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

    @Test
    void call_WithoutTags_DefaultsToEmptyTagListAndReturnsZero() {
        OdinOrchestrator stubOdin = new OdinOrchestrator() {
            @Override
            public OdinResponse run(OdinRequest request) {
                return new OdinResponse(
                        true,
                        "local",
                        "http://localhost:8080",
                        1,
                        1,
                        0,
                        10L,
                        "",
                        "OK"
                );
            }
        };
        CommandLine cmd = new CommandLine(new RunTestsCommand(stubOdin));

        int exitCode = cmd.execute("-e", "local");

        assertEquals(0, exitCode);
    }

    @Test
    void call_WhenExecutionFails_ReturnsExitCodeOne() {
        OdinOrchestrator stubOdin = new OdinOrchestrator() {
            @Override
            public OdinResponse run(OdinRequest request) {
                return new OdinResponse(
                        false,
                        "local",
                        "http://localhost:8080",
                        1,
                        0,
                        1,
                        10L,
                        "",
                        "Failed"
                );
            }
        };
        CommandLine cmd = new CommandLine(new RunTestsCommand(stubOdin));

        int exitCode = cmd.execute("-e", "local", "-t", "smoke");

        assertEquals(1, exitCode);
    }
}