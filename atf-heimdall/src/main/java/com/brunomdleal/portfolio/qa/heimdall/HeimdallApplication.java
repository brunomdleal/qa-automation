package com.brunomdleal.portfolio.qa.heimdall;

import com.brunomdleal.portfolio.qa.heimdall.cli.RunTestsCommand;
import picocli.CommandLine;

import java.util.function.IntConsumer;

public final class HeimdallApplication {

    static IntConsumer exitHandler = System::exit;

    private HeimdallApplication() {
    }

    public static void main(String[] args) {
        int exitCode = execute(args);
        exitHandler.accept(exitCode);
    }

    public static int execute(String[] args) {
        return new CommandLine(new RunTestsCommand()).execute(args);
    }
}