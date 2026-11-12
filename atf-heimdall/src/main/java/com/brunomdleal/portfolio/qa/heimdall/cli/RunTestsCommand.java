package com.brunomdleal.portfolio.qa.heimdall.cli;

import com.brunomdleal.portfolio.qa.heimdall.model.HeimdallRequest;
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
    public Integer call() throws Exception {
        HeimdallRequest request = new HeimdallRequest(environment, baseUrl, tags, parallel);

        renderHeader();
        renderRequestSummary(request);

        // Standalone stub response — will be replaced by OdinOrchestrator call in the next step
        System.out.println("\n[HEIMDALL] Input validated. Ready to dispatch to Odin.");
        System.out.println("[STATUS]   SUCCESS (Standalone Gatekeeper Check)");
        return 0;
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
}
