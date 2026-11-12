package com.brunomdleal.portfolio.qa.heimdall.model;

import java.util.List;

public record HeimdallRequest(
        String environment,
        String baseUrl,
        List<String> tags,
        boolean parallel
) {
}
