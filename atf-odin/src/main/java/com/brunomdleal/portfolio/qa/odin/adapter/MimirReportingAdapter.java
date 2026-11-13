package com.brunomdleal.portfolio.qa.odin.adapter;

import com.brunomdleal.portfolio.qa.mimir.MimirReportEngine;
import com.brunomdleal.portfolio.qa.mimir.model.MimirReportRequest;
import com.brunomdleal.portfolio.qa.mimir.model.MimirReportResult;
import com.brunomdleal.portfolio.qa.odin.model.ExecutionOutcome;
import com.brunomdleal.portfolio.qa.odin.model.ExecutionPlan;
import com.brunomdleal.portfolio.qa.odin.model.OdinResponse;
import com.brunomdleal.portfolio.qa.odin.port.ReportingPort;

public class MimirReportingAdapter implements ReportingPort {

    private final MimirReportEngine reportEngine;

    public MimirReportingAdapter() {
        this(new MimirReportEngine());
    }

    public MimirReportingAdapter(MimirReportEngine reportEngine) {
        this.reportEngine = reportEngine;
    }

    @Override
    public OdinResponse generateReport(ExecutionPlan plan, ExecutionOutcome outcome) {
        MimirReportRequest request = new MimirReportRequest(
                plan.environment(),
                plan.baseUrl(),
                plan.tags(),
                plan.parallel(),
                outcome.totalTests(),
                outcome.passedTests(),
                outcome.failedTests(),
                outcome.durationMillis()
        );

        MimirReportResult result = reportEngine.generate(request);

        return new OdinResponse(
                result.success(),
                plan.environment(),
                plan.baseUrl(),
                outcome.totalTests(),
                outcome.passedTests(),
                outcome.failedTests(),
                outcome.durationMillis(),
                request.outputDirectory().toString(),
                result.consoleSummary()
        );
    }
}