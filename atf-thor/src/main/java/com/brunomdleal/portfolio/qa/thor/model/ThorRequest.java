package com.brunomdleal.portfolio.qa.thor.model;

import java.util.List;

public record ThorRequest(
        String environment,
        String baseUrl,
        String dbUrl,
        List<String> tags,
        boolean parallel,
        String scanPackage
) {
    public ThorRequest(String environment, String baseUrl, String dbUrl, List<String> tags, boolean parallel) {
        this(environment, baseUrl, dbUrl, tags, parallel, "com.brunomdleal.portfolio.qa");
    }
}