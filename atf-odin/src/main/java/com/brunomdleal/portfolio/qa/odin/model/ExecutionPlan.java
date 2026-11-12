package com.brunomdleal.portfolio.qa.odin.model;

import java.util.List;

public record ExecutionPlan(
        String environment,
        String baseUrl,
        String dbUrl,
        List<String> tags,
        boolean parallel
) {
}
