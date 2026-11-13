package com.brunomdleal.portfolio.qa.odin.adapter;

import com.brunomdleal.portfolio.qa.mimir.MimirReportEngine;
import com.brunomdleal.portfolio.qa.mimir.model.MimirReportRequest;
import com.brunomdleal.portfolio.qa.mimir.model.MimirReportResult;
import com.brunomdleal.portfolio.qa.odin.model.ExecutionOutcome;
import com.brunomdleal.portfolio.qa.odin.model.ExecutionPlan;
import com.brunomdleal.portfolio.qa.odin.model.OdinResponse;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MimirReportingAdapterTest {

    @Test
    void generateReport_DelegatesToMimirAndMapsOdinResponse() {
        MimirReportEngine stubEngine = new MimirReportEngine() {
            @Override
            public MimirReportResult generate(MimirReportRequest request) {
                return new MimirReportResult(
                        request.isSuccess(),
                        "[MIMIR REPORT] Status: PASSED",
                        Path.of("target/atf-reports/report.json"),
                        Path.of("target/atf-reports/report.html")
                );
            }
        };

        MimirReportingAdapter adapter = new MimirReportingAdapter(stubEngine);
        ExecutionPlan plan = new ExecutionPlan(
                "local",
                "http://localhost:8080",
                "jdbc:postgresql://localhost:5432/orderdb",
                List.of("smoke"),
                false
        );
        ExecutionOutcome outcome = new ExecutionOutcome(3, 3, 0, 85L);

        OdinResponse response = adapter.generateReport(plan, outcome);

        assertTrue(response.success());
        assertEquals("local", response.environment());
        assertEquals(3, response.totalTests());
        assertEquals("[MIMIR REPORT] Status: PASSED", response.summaryMessage());
    }
}