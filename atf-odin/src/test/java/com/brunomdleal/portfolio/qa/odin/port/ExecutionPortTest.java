package com.brunomdleal.portfolio.qa.odin.port;

import com.brunomdleal.portfolio.qa.odin.model.ExecutionOutcome;
import com.brunomdleal.portfolio.qa.odin.model.ExecutionPlan;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExecutionPortTest {

    private final ExecutionPlan samplePlan = new ExecutionPlan(
            "local",
            "http://localhost:8080",
            "jdbc:postgresql://localhost:5432/orderdb",
            List.of("smoke"),
            false
    );

    @Test
    void executionPort_StandaloneStub_ReturnsDefaultPassingOutcome() {
        ExecutionPort stub = ExecutionPort.standaloneStub();

        ExecutionOutcome outcome = stub.execute(samplePlan);

        assertTrue(outcome.isSuccess());
        assertEquals(0, outcome.failedTests());
    }
}
