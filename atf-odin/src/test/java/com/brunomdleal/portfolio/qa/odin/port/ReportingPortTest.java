package com.brunomdleal.portfolio.qa.odin.port;

import com.brunomdleal.portfolio.qa.odin.model.ExecutionOutcome;
import com.brunomdleal.portfolio.qa.odin.model.ExecutionPlan;
import com.brunomdleal.portfolio.qa.odin.model.OdinResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

public class ReportingPortTest {

    private final ExecutionPlan samplePlan = new ExecutionPlan(
            "local",
            "http://localhost:8080",
            "jdbc:postgresql://localhost:5432/orderdb",
            List.of("smoke"),
            false
    );

    @Test
    void reportingPort_StandaloneStub_CoversPassingAndFailingOutcomes() {
        ReportingPort stub = ReportingPort.standaloneStub();

        OdinResponse passResponse = stub.generateReport(samplePlan, new ExecutionOutcome(1, 1, 0, 20L));
        OdinResponse failResponse = stub.generateReport(samplePlan, new ExecutionOutcome(1, 0, 1, 20L));

        assertTrue(passResponse.success());
        assertFalse(failResponse.success());
    }
}
