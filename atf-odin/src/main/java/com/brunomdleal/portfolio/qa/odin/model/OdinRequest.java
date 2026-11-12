package com.brunomdleal.portfolio.qa.odin.model;

import java.util.List;

public record OdinRequest(
        String environment,
        String baseUrlOverride,
        List<String> tags,
        boolean parallel
) {
}
