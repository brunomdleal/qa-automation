package com.brunomdleal.portfolio.qa.mimir;

import com.brunomdleal.portfolio.qa.mimir.model.MimirReportRequest;
import com.brunomdleal.portfolio.qa.mimir.model.MimirReportResult;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

public class MimirReportEngine {

    public MimirReportResult generate(MimirReportRequest request) {
        try {
            Files.createDirectories(request.outputDirectory());

            String consoleSummary = renderConsoleSummary(request);
            Path jsonPath = request.outputDirectory().resolve("report.json");
            Path htmlPath = request.outputDirectory().resolve("report.html");

            Files.writeString(jsonPath, renderJson(request));
            Files.writeString(htmlPath, renderHtml(request));

            return new MimirReportResult(
                    request.isSuccess(),
                    consoleSummary,
                    jsonPath,
                    htmlPath
            );
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write Mimir test reports", e);
        }
    }

    private String renderConsoleSummary(MimirReportRequest req) {
        String status = req.isSuccess() ? "PASSED" : "FAILED";
        String tagsDisplay = formatTags(req.tags());
        return String.format(
                "[MIMIR REPORT] Status: %s | Env: %s (%s) | Tags: %s | Parallel: %s | Total: %d, Passed: %d, Failed: %d | Duration: %d ms",
                status,
                req.environment(),
                req.baseUrl(),
                tagsDisplay,
                req.parallel(),
                req.totalTests(),
                req.passedTests(),
                req.failedTests(),
                req.durationMillis()
        );
    }

    private String renderJson(MimirReportRequest req) {
        String status = req.isSuccess() ? "PASSED" : "FAILED";
        List<String> safeTags = req.tags() == null ? List.of() : req.tags();
        String jsonTags = safeTags.stream()
                .map(t -> "\"" + escapeJson(t) + "\"")
                .reduce((a, b) -> a + ", " + b)
                .orElse("");

        return """
                {
                  "timestamp": "%s",
                  "status": "%s",
                  "environment": "%s",
                  "baseUrl": "%s",
                  "tags": [%s],
                  "parallel": %s,
                  "metrics": {
                    "totalTests": %d,
                    "passedTests": %d,
                    "failedTests": %d,
                    "durationMillis": %d
                  }
                }
                """.formatted(
                Instant.now().toString(),
                status,
                escapeJson(req.environment()),
                escapeJson(req.baseUrl()),
                jsonTags,
                req.parallel(),
                req.totalTests(),
                req.passedTests(),
                req.failedTests(),
                req.durationMillis()
        );
    }

    private String renderHtml(MimirReportRequest req) {
        String status = req.isSuccess() ? "PASSED" : "FAILED";
        String badgeColor = req.isSuccess() ? "#16a34a" : "#dc2626";
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                  <meta charset="UTF-8">
                  <title>Mimir ATF Execution Report</title>
                  <style>
                    body { font-family: system-ui, sans-serif; margin: 2rem; background: #0f172a; color: #f8fafc; }
                    .card { background: #1e293b; padding: 1.5rem; border-radius: 8px; max-width: 720px; }
                    .badge { display: inline-block; padding: 0.25rem 0.75rem; border-radius: 999px; font-weight: bold; background: %s; }
                    table { width: 100%%; border-collapse: collapse; margin-top: 1rem; }
                    td, th { text-align: left; padding: 0.5rem; border-bottom: 1px solid #334155; }
                  </style>
                </head>
                <body>
                  <div class="card">
                    <h1>Mimir ATF Report <span class="badge">%s</span></h1>
                    <table>
                      <tr><th>Environment</th><td>%s</td></tr>
                      <tr><th>Base URL</th><td>%s</td></tr>
                      <tr><th>Tags</th><td>%s</td></tr>
                      <tr><th>Parallel</th><td>%s</td></tr>
                      <tr><th>Total / Passed / Failed</th><td>%d / %d / %d</td></tr>
                      <tr><th>Duration</th><td>%d ms</td></tr>
                    </table>
                  </div>
                </body>
                </html>
                """.formatted(
                badgeColor,
                status,
                escapeHtml(req.environment()),
                escapeHtml(req.baseUrl()),
                escapeHtml(formatTags(req.tags())),
                req.parallel(),
                req.totalTests(),
                req.passedTests(),
                req.failedTests(),
                req.durationMillis()
        );
    }

    private String formatTags(List<String> tags) {
        return (tags == null || tags.isEmpty()) ? "all" : String.join(",", tags);
    }

    private String escapeJson(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String escapeHtml(String value) {
        return value == null ? "" : value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}