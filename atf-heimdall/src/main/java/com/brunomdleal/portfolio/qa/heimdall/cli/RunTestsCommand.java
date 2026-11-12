package com.brunomdleal.portfolio.qa.heimdall.cli;

import com.brunomdleal.portfolio.qa.heimdall.model.HeimdallRequest;
import com.brunomdleal.portfolio.qa.odin.OdinOrchestrator;
import com.brunomdleal.portfolio.qa.odin.model.OdinRequest;
import com.brunomdleal.portfolio.qa.odin.model.OdinResponse;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.List;
import java.util.concurrent.Callable;

@Command(
        name = "heimdall",
        mixinStandardHelpOptions = true,
        versionProvider = PomVersionProvider.class,
        description = "Guardian of the Bifröst — validates ATF inputs and dispatches test runs."
)
public class RunTestsCommand implements Callable<Integer> {

    private final OdinOrchestrator odinOrchestrator;

    public RunTestsCommand() {
        this(new OdinOrchestrator());
    }

    RunTestsCommand(OdinOrchestrator odinOrchestrator) {
        this.odinOrchestrator = odinOrchestrator;
    }

    @Option(
            names = {"-e", "--env"},
            description = "Target environment: local, dev, prod (default: ${DEFAULT-VALUE})",
            defaultValue = "local"
    )
    private String environment;

    @Option(
            names = {"-u", "--base-url"},
            description = "Explicit target base URL override (e.g., http://localhost:8080)"
    )
    private String baseUrl;

    @Option(
            names = {"-t", "--tags"},
            split = ",",
            description = "Comma-separated test suite tags: api, ui, db, perf, chaos, security (default: ${DEFAULT-VALUE})",
            defaultValue = "smoke"
    )
    private List<String> tags;

    @Option(
            names = {"-p", "--parallel"},
            description = "Enable parallel suite execution (default: ${DEFAULT-VALUE})",
            defaultValue = "false"
    )
    private boolean parallel;

    @Override
    public Integer call() {
        HeimdallRequest request = new HeimdallRequest(environment, baseUrl, tags, parallel);

        renderHeader();
        renderRequestSummary(request);

        OdinRequest odinRequest = new OdinRequest(
                request.environment(),
                request.baseUrl(),
                request.tags(),
                request.parallel()
        );

        OdinResponse odinResponse = odinOrchestrator.run(odinRequest);

        renderExecutionReport(odinResponse);
        return odinResponse.success() ? 0 : 1;
    }

    private void renderHeader() {
        System.out.println("==================================================");
        System.out.println("   HEIMDALL — Automated Test Framework (Input)    ");
        System.out.println("==================================================");
    }

    private void renderRequestSummary(HeimdallRequest request) {
        System.out.printf("%-12s: %s%n", "Environment", request.environment());
        System.out.printf(
                "%-12s: %s%n",
                "Base URL",
                request.baseUrl() != null ? request.baseUrl() : "(auto-resolve by Odin)"
        );
        System.out.printf("%-12s: %s%n", "Tags", String.join(", ", request.tags()));
        System.out.printf("%-12s: %s%n", "Parallel", request.parallel());
    }

    private void renderExecutionReport(OdinResponse response) {
        System.out.println("--------------------------------------------------");
        System.out.printf("%-12s: %s%n", "Target URL", response.resolvedBaseUrl());
        System.out.printf("%-12s: %d total | %d passed | %d failed (%dms)%n",
                "Results",
                response.totalTests(),
                response.passedTests(),
                response.failedTests(),
                response.durationMillis());
        System.out.printf("%-12s: %s%n", "Report", response.reportPath());
        System.out.printf("%-12s: %s%n", "Summary", response.summaryMessage());
        System.out.printf("%-12s: %s%n", "Status", response.success() ? "SUCCESS" : "FAILURE");
    }
}
