package com.brunomdleal.portfolio.qa.thor;

import com.brunomdleal.portfolio.qa.thor.context.ExecutionContext;
import com.brunomdleal.portfolio.qa.thor.model.TestFailureDetail;
import com.brunomdleal.portfolio.qa.thor.model.ThorRequest;
import com.brunomdleal.portfolio.qa.thor.model.ThorResult;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TagFilter;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.util.List;

public class ThorExecutor {

    //  Holds the JUnit Platform Launcher instance responsible for discovering and executing tests
    private final Launcher launcher;

    public ThorExecutor() {
        this(LauncherFactory.create());
    }

    ThorExecutor(Launcher launcher) {
        this.launcher = launcher;
    }

    public ThorResult execute(ThorRequest request) {
        ExecutionContext.set(request);
        try {
            // Filtered tests ID and their localization
            LauncherDiscoveryRequest discoveryRequest = buildDiscoveryRequest(request);
            // Tests reporter
            SummaryGeneratingListener listener = new SummaryGeneratingListener();
            // Executes the filtered tests ID and their localization with the evaluator reporting them
            launcher.execute(discoveryRequest, listener);
            // Hands over the tests report
            TestExecutionSummary summary = listener.getSummary();
            return mapToResult(summary);
        } finally {
            ExecutionContext.clear();
        }
    }

    // Builds the request with all the info in order to JUnit to knows where and which test should look for
    private LauncherDiscoveryRequest buildDiscoveryRequest(ThorRequest request) {
        LauncherDiscoveryRequestBuilder builder = LauncherDiscoveryRequestBuilder.request();
        builder.selectors(
                // Identifies where the files containing "@Test" exists
                DiscoverySelectors.selectPackage(
                        // Passes the scanPackage within the ThorRequest
                        request.scanPackage()
                )
        );

        if (request.tags() != null && !request.tags().isEmpty()) {
            // Selects the tests that follow this rules
            builder.filters(
                    // Check if contains the tags within the ThorRequest
                    TagFilter.includeTags(request.tags())
            );
        }

        if (request.parallel()) {
            builder.configurationParameter("junit.jupiter.execution.parallel.enabled", "true");
            builder.configurationParameter("junit.jupiter.execution.parallel.mode.default", "concurrent");
        }

        return builder.build();
    }

    private ThorResult mapToResult(TestExecutionSummary summary) {
        int total = (int) summary.getTestsFoundCount();
        int passed = (int) summary.getTestsSucceededCount();
        int failed = (int) summary.getTestsFailedCount();
        long duration = summary.getTimeFinished() - summary.getTimeStarted();

        List<TestFailureDetail> failures = summary.getFailures().stream()
                .map(failure -> new TestFailureDetail(
                        failure.getTestIdentifier().getDisplayName(),
                        failure.getException().getMessage()
                ))
                .toList();

        return new ThorResult(total, passed, failed, duration, failures);
    }
}