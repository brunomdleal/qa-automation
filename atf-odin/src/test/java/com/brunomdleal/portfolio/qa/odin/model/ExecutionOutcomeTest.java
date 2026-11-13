package com.brunomdleal.portfolio.qa.odin.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExecutionOutcomeTest {

    @Test
    void executionOutcome_IsSuccess_CoversBothBranches() {
        ExecutionOutcome passing = new ExecutionOutcome(2, 2, 0, 50L);
        ExecutionOutcome failing = new ExecutionOutcome(2, 1, 1, 50L);

        assertTrue(passing.isSuccess());
        assertFalse(failing.isSuccess());
    }
}
