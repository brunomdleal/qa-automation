package com.brunomdleal.portfolio.qa.odin.port;

import com.brunomdleal.portfolio.qa.odin.model.ExecutionOutcome;
import com.brunomdleal.portfolio.qa.odin.model.ExecutionPlan;
import com.brunomdleal.portfolio.qa.odin.model.OdinResponse;

@FunctionalInterface
public interface ReportingPort {

    OdinResponse generateReport(ExecutionPlan plan, ExecutionOutcome outcome);

    static ReportingPort standaloneStub() {
        return (plan, outcome) -> new OdinResponse(
                outcome.isSuccess(),
                plan.environment(),
                plan.baseUrl(),
                outcome.totalTests(),
                outcome.passedTests(),
                outcome.failedTests(),
                outcome.durationMillis(),
                "target/reports/odin-summary.json",
                outcome.isSuccess()
                        ? "Orchestration completed cleanly (standalone stub)"
                        : "Orchestration completed with test failures"
        );
    }
}