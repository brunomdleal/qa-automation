package com.brunomdleal.portfolio.qa.mimir.model;

import java.nio.file.Path;
import java.util.List;

public record MimirReportRequest(
        String environment,
        String baseUrl,
        List<String> tags,
        boolean parallel,
        int totalTests,
        int passedTests,
        int failedTests,
        long durationMillis,
        Path outputDirectory
) {
    public MimirReportRequest(
            String environment,
            String baseUrl,
            List<String> tags,
            boolean parallel,
            int totalTests,
            int passedTests,
            int failedTests,
            long durationMillis
    ) {
        this(
                environment,
                baseUrl,
                tags,
                parallel,
                totalTests,
                passedTests,
                failedTests,
                durationMillis,
                Path.of("target", "atf-reports")
        );
    }

    public boolean isSuccess() {
        return failedTests == 0;
    }
}