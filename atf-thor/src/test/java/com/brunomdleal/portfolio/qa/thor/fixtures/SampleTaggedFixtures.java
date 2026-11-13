package com.brunomdleal.portfolio.qa.thor.fixtures;

import com.brunomdleal.portfolio.qa.thor.context.ExecutionContext;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SampleTaggedFixtures {

    @Test
    @Tag("thor-smoke")
    void passingSmokeFixture_ReadsExecutionContext() {
        assertEquals("http://localhost:8080", ExecutionContext.get().baseUrl());
    }

    @Test
    @Tag("thor-fail")
    void failingFixture_CapturesFailureMessage() {
        fail("Simulated assertion failure in Thor fixture");
    }
}