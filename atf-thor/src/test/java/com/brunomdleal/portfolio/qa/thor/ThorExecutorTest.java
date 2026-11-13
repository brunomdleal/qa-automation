package com.brunomdleal.portfolio.qa.thor;

import com.brunomdleal.portfolio.qa.thor.context.ExecutionContext;
import com.brunomdleal.portfolio.qa.thor.model.ThorRequest;
import com.brunomdleal.portfolio.qa.thor.model.ThorResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ThorExecutorTest {

    private static final String FIXTURES_PACKAGE = "com.brunomdleal.portfolio.qa.thor.fixtures";

    @Test
    void execute_WithPassingTag_ExecutesMatchingSuiteAndClearsContext() {
        ThorExecutor thor = new ThorExecutor();
        ThorRequest request = new ThorRequest(
                "local",
                "http://localhost:8080",
                "jdbc:postgresql://localhost:5432/orderdb",
                List.of("thor-smoke"),
                false,
                FIXTURES_PACKAGE
        );

        ThorResult result = thor.execute(request);

        assertTrue(result.isSuccess());
        assertEquals(1, result.totalTests());
        assertEquals(1, result.passedTests());
        assertEquals(0, result.failedTests());
        assertTrue(result.failures().isEmpty());
        assertThrows(IllegalStateException.class, ExecutionContext::get);
    }

    @Test
    void execute_WithFailingTagAndParallelEnabled_CapturesFailureDetails() {
        ThorExecutor thor = new ThorExecutor();
        ThorRequest request = new ThorRequest(
                "dev",
                "http://dev.order.local",
                "jdbc:postgresql://dev.order.local:5432/orderdb",
                List.of("thor-fail"),
                true,
                FIXTURES_PACKAGE
        );

        ThorResult result = thor.execute(request);

        assertFalse(result.isSuccess());
        assertEquals(1, result.totalTests());
        assertEquals(0, result.passedTests());
        assertEquals(1, result.failedTests());
        assertEquals(1, result.failures().size());
        assertEquals("Simulated assertion failure in Thor fixture", result.failures().getFirst().errorMessage());
    }

    @Test
    void execute_WithEmptyTags_RunsAllFixturesInPackage() {
        ThorExecutor thor = new ThorExecutor();
        ThorRequest request = new ThorRequest(
                "local",
                "http://localhost:8080",
                "jdbc:postgresql://localhost:5432/orderdb",
                List.of(),
                false,
                FIXTURES_PACKAGE
        );

        ThorResult result = thor.execute(request);

        assertFalse(result.isSuccess());
        assertEquals(2, result.totalTests());
        assertEquals(1, result.passedTests());
        assertEquals(1, result.failedTests());
    }

    @Test
    void execute_WithNullTags_RunsAllFixturesInPackage() {
        ThorExecutor thor = new ThorExecutor();
        ThorRequest request = new ThorRequest(
                "local",
                "http://localhost:8080",
                "jdbc:postgresql://localhost:5432/orderdb",
                null,
                false,
                FIXTURES_PACKAGE
        );

        ThorResult result = thor.execute(request);

        assertFalse(result.isSuccess());
        assertEquals(2, result.totalTests());
        assertEquals(1, result.passedTests());
        assertEquals(1, result.failedTests());
    }

    @Test
    void thorRequest_FiveArgConstructor_DefaultsScanPackage() {
        ThorRequest request = new ThorRequest(
                "local",
                "http://localhost:8080",
                "jdbc:postgresql://localhost:5432/orderdb",
                List.of("smoke"),
                false
        );

        assertEquals("com.brunomdleal.portfolio.qa", request.scanPackage());
    }
}