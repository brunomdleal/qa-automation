package com.brunomdleal.portfolio.qa.odin.adapter;

import com.brunomdleal.portfolio.qa.odin.model.ExecutionOutcome;
import com.brunomdleal.portfolio.qa.odin.model.ExecutionPlan;
import com.brunomdleal.portfolio.qa.odin.port.ExecutionPort;
import com.brunomdleal.portfolio.qa.thor.ThorExecutor;
import com.brunomdleal.portfolio.qa.thor.model.ThorRequest;
import com.brunomdleal.portfolio.qa.thor.model.ThorResult;

public class ThorExecutionAdapter implements ExecutionPort {

    private final ThorExecutor thorExecutor;

    public ThorExecutionAdapter() {
        this(new ThorExecutor());
    }

    public ThorExecutionAdapter(ThorExecutor thorExecutor) {
        this.thorExecutor = thorExecutor;
    }

    @Override
    public ExecutionOutcome execute(ExecutionPlan plan) {
        ThorRequest request = new ThorRequest(
                plan.environment(),
                plan.baseUrl(),
                plan.dbUrl(),
                plan.tags(),
                plan.parallel()
        );

        ThorResult result = thorExecutor.execute(request);

        return new ExecutionOutcome(
                result.totalTests(),
                result.passedTests(),
                result.failedTests(),
                result.durationMillis()
        );
    }
}