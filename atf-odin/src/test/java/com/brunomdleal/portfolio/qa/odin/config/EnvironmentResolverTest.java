package com.brunomdleal.portfolio.qa.odin.config;

import com.brunomdleal.portfolio.qa.odin.model.ExecutionPlan;
import com.brunomdleal.portfolio.qa.odin.model.OdinRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnvironmentResolverTest {

    private final EnvironmentResolver resolver = new EnvironmentResolver();

    @Test
    void resolve_WithNullAndBlankDefaults_FallsBackToLocalAndSmoke() {
        ExecutionPlan nullEnvPlan = resolver.resolve(new OdinRequest(null, null, null, false));
        assertEquals("local", nullEnvPlan.environment());
        assertEquals("http://localhost:8080", nullEnvPlan.baseUrl());
        assertEquals("jdbc:postgresql://localhost:5432/orderdb", nullEnvPlan.dbUrl());
        assertEquals(List.of("smoke"), nullEnvPlan.tags());

        ExecutionPlan blankEnvPlan = resolver.resolve(new OdinRequest("  ", "  ", List.of(), true));
        assertEquals("local", blankEnvPlan.environment());
        assertEquals("http://localhost:8080", blankEnvPlan.baseUrl());
        assertTrue(blankEnvPlan.parallel());
    }

    @Test
    void resolve_WithDevAndProdEnvironments_ResolvesClusterHosts() {
        ExecutionPlan devPlan = resolver.resolve(new OdinRequest("DEV", null, List.of("api", "db"), false));
        assertEquals("dev", devPlan.environment());
        assertEquals("http://dev.order.local", devPlan.baseUrl());
        assertEquals("jdbc:postgresql://dev.order.local:5432/orderdb", devPlan.dbUrl());

        ExecutionPlan prodPlan = resolver.resolve(new OdinRequest("prod", null, List.of("smoke"), false));
        assertEquals("prod", prodPlan.environment());
        assertEquals("http://prod.order.local", prodPlan.baseUrl());
        assertEquals("jdbc:postgresql://prod.order.local:5432/orderdb", prodPlan.dbUrl());
    }

    @Test
    void resolve_WithBaseUrlOverride_UsesCustomUrl() {
        ExecutionPlan plan = resolver.resolve(new OdinRequest("dev", "http://custom:9090", List.of("ui"), false));
        assertEquals("http://custom:9090", plan.baseUrl());
    }

    @Test
    void resolve_WithUnsupportedEnvironmentAndNullBaseUrl_ThrowsException() {
        OdinRequest invalid = new OdinRequest("staging", null, List.of("smoke"), false);
        assertThrows(IllegalArgumentException.class, () -> resolver.resolve(invalid));
    }

    @Test
    void resolve_WithUnsupportedEnvironment_ThrowsException() {
        OdinRequest invalid = new OdinRequest("staging", "http://custom:9090", List.of("smoke"), false);
        assertThrows(IllegalArgumentException.class, () -> resolver.resolve(invalid));
    }
}