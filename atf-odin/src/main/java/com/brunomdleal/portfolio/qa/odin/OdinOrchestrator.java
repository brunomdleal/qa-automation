package com.brunomdleal.portfolio.qa.odin;

import com.brunomdleal.portfolio.qa.odin.config.EnvironmentResolver;
import com.brunomdleal.portfolio.qa.odin.model.ExecutionOutcome;
import com.brunomdleal.portfolio.qa.odin.model.ExecutionPlan;
import com.brunomdleal.portfolio.qa.odin.model.OdinRequest;
import com.brunomdleal.portfolio.qa.odin.model.OdinResponse;
import com.brunomdleal.portfolio.qa.odin.port.ExecutionPort;
import com.brunomdleal.portfolio.qa.odin.port.ReportingPort;

public class OdinOrchestrator {

    private final EnvironmentResolver environmentResolver;
    private final ExecutionPort executionPort;
    private final ReportingPort reportingPort;

    public OdinOrchestrator() {
        this(new EnvironmentResolver(), ExecutionPort.standaloneStub(), ReportingPort.standaloneStub());
    }

    public OdinOrchestrator(
            EnvironmentResolver environmentResolver,
            ExecutionPort executionPort,
            ReportingPort reportingPort
    ) {
        this.environmentResolver = environmentResolver;
        this.executionPort = executionPort;
        this.reportingPort = reportingPort;
    }

    public OdinResponse run(OdinRequest request) {
        ExecutionPlan plan = environmentResolver.resolve(request);
        ExecutionOutcome outcome = executionPort.execute(plan);
        return reportingPort.generateReport(plan, outcome);
    }
}