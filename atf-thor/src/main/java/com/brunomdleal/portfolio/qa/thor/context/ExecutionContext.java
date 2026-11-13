package com.brunomdleal.portfolio.qa.thor.context;

import com.brunomdleal.portfolio.qa.thor.model.ThorRequest;

public final class ExecutionContext {

    // Thread-safe holder that propagates the active ThorRequest to test threads (including child threads during parallel execution).
    private static final InheritableThreadLocal<ThorRequest> CURRENT_REQUEST = new InheritableThreadLocal<>();

    private ExecutionContext() {}

    public static void set(ThorRequest request) {
        CURRENT_REQUEST.set(request);
    }

    // Responsible to get the `ThorRequest` from the thread that is called from
    public static ThorRequest get() {
        ThorRequest request = CURRENT_REQUEST.get();
        if (request == null) {
            throw new IllegalStateException("ExecutionContext has not been initialized for the current test run.");
        }
        return request;
    }

    public static void clear() {
        CURRENT_REQUEST.remove();
    }
}