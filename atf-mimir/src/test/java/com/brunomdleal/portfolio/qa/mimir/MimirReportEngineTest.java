package com.brunomdleal.portfolio.qa.mimir;

import com.brunomdleal.portfolio.qa.mimir.model.MimirReportRequest;
import com.brunomdleal.portfolio.qa.mimir.model.MimirReportResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MimirReportEngineTest {

    private final MimirReportEngine engine = new MimirReportEngine();

    @Test
    void generate_WithPassingSuiteAndTags_CreatesJsonAndHtmlReports(@TempDir Path tempDir) throws IOException {
        MimirReportRequest request = new MimirReportRequest(
                "local",
                "http://localhost:8080",
                List.of("smoke", "api"),
                true,
                4,
                4,
                0,
                125L,
                tempDir
        );

        MimirReportResult result = engine.generate(request);

        assertTrue(result.success());
        assertTrue(result.consoleSummary().contains("Status: PASSED"));
        assertTrue(result.consoleSummary().contains("Tags: smoke,api"));
        assertTrue(Files.exists(result.jsonReportPath()));
        assertTrue(Files.exists(result.htmlReportPath()));

        String json = Files.readString(result.jsonReportPath());
        assertTrue(json.contains("\"status\": \"PASSED\""));
        assertTrue(json.contains("\"tags\": [\"smoke\", \"api\"]"));

        String html = Files.readString(result.htmlReportPath());
        assertTrue(html.contains("#16a34a"));
        assertTrue(html.contains("PASSED"));
    }

    @Test
    void generate_WithFailingSuiteAndEmptyTags_RendersFailedBadgeAndAllTags(@TempDir Path tempDir) throws IOException {
        MimirReportRequest request = new MimirReportRequest(
                "dev",
                "http://dev.order.local",
                List.of(),
                false,
                3,
                2,
                1,
                240L,
                tempDir
        );

        MimirReportResult result = engine.generate(request);

        assertFalse(result.success());
        assertTrue(result.consoleSummary().contains("Status: FAILED"));
        assertTrue(result.consoleSummary().contains("Tags: all"));

        String json = Files.readString(result.jsonReportPath());
        assertTrue(json.contains("\"status\": \"FAILED\""));
        assertTrue(json.contains("\"tags\": []"));

        String html = Files.readString(result.htmlReportPath());
        assertTrue(html.contains("#dc2626"));
        assertTrue(html.contains("FAILED"));
    }

    @Test
    void generate_WithNullTagsAndNullStrings_HandlesEscapingSafely(@TempDir Path tempDir) throws IOException {
        MimirReportRequest request = new MimirReportRequest(
                null,
                "http://localhost:8080?a=1&b=<test>\"quote\"\\slash",
                null,
                false,
                1,
                1,
                0,
                10L,
                tempDir
        );

        MimirReportResult result = engine.generate(request);

        assertTrue(result.success());
        assertTrue(result.consoleSummary().contains("Tags: all"));

        String json = Files.readString(result.jsonReportPath());
        assertTrue(json.contains("\\\"quote\\\"\\\\slash"));

        String html = Files.readString(result.htmlReportPath());
        assertTrue(html.contains("&amp;b=&lt;test&gt;"));
    }

    @Test
    void generate_WhenOutputDirectoryIsAFile_ThrowsUncheckedIOException(@TempDir Path tempDir) throws IOException {
        Path blockingFile = tempDir.resolve("not-a-directory");
        Files.writeString(blockingFile, "content");

        MimirReportRequest request = new MimirReportRequest(
                "local",
                "http://localhost:8080",
                List.of("smoke"),
                false,
                1,
                1,
                0,
                10L,
                blockingFile
        );

        assertThrows(UncheckedIOException.class, () -> engine.generate(request));
    }

    @Test
    void requestConvenienceConstructor_DefaultsToTargetAtfReports() {
        MimirReportRequest request = new MimirReportRequest(
                "prod",
                "http://order.local",
                List.of("regression"),
                true,
                10,
                10,
                0,
                500L
        );

        assertEquals(Path.of("target", "atf-reports"), request.outputDirectory());
        assertTrue(request.isSuccess());
    }
}