package com.brunomdleal.portfolio.qa.odin.model;

public record ExecutionOutcome(
        int totalTests,
        int passedTests,
        int failedTests,
        long durationMillis
) {
    public boolean isSuccess() {
        return failedTests == 0;
    }
}
