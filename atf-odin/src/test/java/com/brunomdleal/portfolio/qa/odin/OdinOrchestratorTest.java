package com.brunomdleal.portfolio.qa.odin;

import com.brunomdleal.portfolio.qa.odin.config.EnvironmentResolver;
import com.brunomdleal.portfolio.qa.odin.model.ExecutionOutcome;
import com.brunomdleal.portfolio.qa.odin.model.OdinRequest;
import com.brunomdleal.portfolio.qa.odin.model.OdinResponse;
import com.brunomdleal.portfolio.qa.odin.port.ExecutionPort;
import com.brunomdleal.portfolio.qa.odin.port.ReportingPort;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OdinOrchestratorTest {

    @Test
    void run_WithDefaultStandaloneStubs_ReturnsSuccessResponse() {
        OdinOrchestrator odin = new OdinOrchestrator();
        OdinResponse response = odin.run(new OdinRequest("local", null, List.of("smoke"), false));

        assertTrue(response.success());
        assertEquals("local", response.environment());
        assertEquals("http://localhost:8080", response.resolvedBaseUrl());
        assertEquals(0, response.totalTests());
    }

    @Test
    void run_WhenExecutionHasFailures_ReturnsFailureResponse() {
        ExecutionPort failingThor = plan -> new ExecutionOutcome(5, 3, 2, 150L);
        OdinOrchestrator odin = new OdinOrchestrator(
                new EnvironmentResolver(),
                failingThor,
                ReportingPort.standaloneStub()
        );

        OdinResponse response = odin.run(new OdinRequest("dev", null, List.of("api"), true));

        assertFalse(response.success());
        assertEquals(5, response.totalTests());
        assertEquals(3, response.passedTests());
        assertEquals(2, response.failedTests());
        assertEquals(150L, response.durationMillis());
    }
}