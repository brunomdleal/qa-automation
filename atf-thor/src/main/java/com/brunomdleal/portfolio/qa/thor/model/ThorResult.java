package com.brunomdleal.portfolio.qa.thor.model;

import java.util.List;

public record ThorResult(
        int totalTests,
        int passedTests,
        int failedTests,
        long durationMillis,
        List<TestFailureDetail> failures
) {
    public boolean isSuccess() {
        return failedTests == 0;
    }
}