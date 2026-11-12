package com.brunomdleal.portfolio.qa.odin.model;

public record OdinResponse(
        boolean success,
        String environment,
        String resolvedBaseUrl,
        int totalTests,
        int passedTests,
        int failedTests,
        long durationMillis,
        String reportPath,
        String summaryMessage
) {
}
