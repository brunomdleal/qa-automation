package com.brunomdleal.portfolio.qa.thor.model;

public record TestFailureDetail(
        String testDisplayName,
        String errorMessage
) {
}