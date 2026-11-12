package com.brunomdleal.portfolio.qa.odin.port;

import com.brunomdleal.portfolio.qa.odin.model.ExecutionOutcome;
import com.brunomdleal.portfolio.qa.odin.model.ExecutionPlan;

@FunctionalInterface
public interface ExecutionPort {

    ExecutionOutcome execute(ExecutionPlan plan);

    static ExecutionPort standaloneStub() {
        return plan -> new ExecutionOutcome(0, 0, 0, 0L);
    }
}