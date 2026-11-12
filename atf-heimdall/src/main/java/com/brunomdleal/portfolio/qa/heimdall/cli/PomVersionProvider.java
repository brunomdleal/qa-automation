package com.brunomdleal.portfolio.qa.heimdall.cli;

import picocli.CommandLine.IVersionProvider;

import java.io.InputStream;
import java.util.Properties;

public class PomVersionProvider implements IVersionProvider {

    private final String resourcePath;

    public PomVersionProvider() {
        this("/version.properties");
    }

    PomVersionProvider(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    @Override
    public String[] getVersion() throws Exception {
        Properties props = new Properties();
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is != null) {
                props.load(is);
            }
        }
        String version = props.getProperty("app.version", "unknown");
        return new String[]{"Heimdall ATF Gatekeeper " + version};
    }


}