package com.brunomdleal.portfolio.qa.odin.adapter;

import com.brunomdleal.portfolio.qa.odin.model.ExecutionOutcome;
import com.brunomdleal.portfolio.qa.odin.model.ExecutionPlan;
import com.brunomdleal.portfolio.qa.thor.ThorExecutor;
import com.brunomdleal.portfolio.qa.thor.model.TestFailureDetail;
import com.brunomdleal.portfolio.qa.thor.model.ThorRequest;
import com.brunomdleal.portfolio.qa.thor.model.ThorResult;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ThorExecutionAdapterTest {

    @Test
    void execute_MapsPassingAndFailingThorResultsAccurately() {
        ThorExecutionAdapter adapter = getAdapter();

        ExecutionOutcome passOutcome = adapter.execute(new ExecutionPlan(
                "local", "http://localhost:8080", "jdbc:postgresql://localhost:5432/orderdb",
                List.of("smoke"), false
        ));
        assertTrue(passOutcome.isSuccess());
        assertEquals(2, passOutcome.totalTests());
        assertEquals(2, passOutcome.passedTests());
        assertEquals(0, passOutcome.failedTests());

        ExecutionOutcome failOutcome = adapter.execute(new ExecutionPlan(
                "local", "http://localhost:8080", "jdbc:postgresql://localhost:5432/orderdb",
                List.of("fail"), true
        ));
        assertFalse(failOutcome.isSuccess());
        assertEquals(1, failOutcome.failedTests());
    }

    private static @NonNull ThorExecutionAdapter getAdapter() {
        ThorExecutor stubThor = new ThorExecutor() {
            @Override
            public ThorResult execute(ThorRequest request) {
                if (request.tags().contains("fail")) {
                    return new ThorResult(
                            2, 1, 1, 45L,
                            List.of(new TestFailureDetail("failingTest()", "Assertion failed"))
                    );
                }
                return new ThorResult(2, 2, 0, 30L, List.of());
            }
        };

        ThorExecutionAdapter adapter = new ThorExecutionAdapter(stubThor);
        return adapter;
    }
}