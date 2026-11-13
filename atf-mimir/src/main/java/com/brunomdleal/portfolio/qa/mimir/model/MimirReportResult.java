package com.brunomdleal.portfolio.qa.mimir.model;

import java.nio.file.Path;

public record MimirReportResult(
        boolean success,
        String consoleSummary,
        Path jsonReportPath,
        Path htmlReportPath
) {
}