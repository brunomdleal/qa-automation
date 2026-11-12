package com.brunomdleal.portfolio.qa.odin.config;

import com.brunomdleal.portfolio.qa.odin.model.ExecutionPlan;
import com.brunomdleal.portfolio.qa.odin.model.OdinRequest;

import java.util.List;
import java.util.Locale;

public class EnvironmentResolver {

    public ExecutionPlan resolve(OdinRequest request) {
        String env = (request.environment() == null || request.environment().isBlank())
                ? "local"
                : request.environment().trim().toLowerCase(Locale.ROOT);

        String resolvedBaseUrl = (request.baseUrlOverride() != null && !request.baseUrlOverride().isBlank())
                ? request.baseUrlOverride().trim()
                : defaultBaseUrlFor(env);

        String resolvedDbUrl = defaultDbUrlFor(env);
        List<String> tags = (request.tags() == null || request.tags().isEmpty())
                ? List.of("smoke")
                : List.copyOf(request.tags());

        return new ExecutionPlan(env, resolvedBaseUrl, resolvedDbUrl, tags, request.parallel());
    }

    private String defaultBaseUrlFor(String env) {
        return switch (env) {
            case "local" -> "http://localhost:8080";
            case "dev" -> "http://dev.order.local";
            case "prod" -> "http://prod.order.local";
            default -> throw new IllegalArgumentException("Unsupported environment: " + env);
        };
    }

    private String defaultDbUrlFor(String env) {
        return switch (env) {
            case "local" -> "jdbc:postgresql://localhost:5432/orderdb";
            case "dev" -> "jdbc:postgresql://dev.order.local:5432/orderdb";
            case "prod" -> "jdbc:postgresql://prod.order.local:5432/orderdb";
            default -> throw new IllegalArgumentException("Unsupported environment: " + env);
        };
    }
}