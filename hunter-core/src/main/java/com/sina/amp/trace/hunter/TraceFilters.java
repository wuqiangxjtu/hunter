package com.sina.amp.trace.hunter;

import java.util.ArrayList;
import java.util.List;

import com.github.kristofa.brave.TraceFilter;


/**
 * Injecting a generic type does not work well due to type erasure. So building a wrapper class around List of TraceFilter.
 * 
 * @author kristof
 */
public class TraceFilters {

    private final List<TraceFilter> filters;

    /**
     * Create a new instance.
     * 
     * @param traceFilters List of Trace Filters.
     */
    public TraceFilters(final List<TraceFilter> traceFilters) {
        filters = new ArrayList<>(traceFilters);
    }

    /**
     * Gets List of {@link TraceFilter trace filters}.
     * 
     * @return List of {@link TraceFilter trace filters}.
     */
    public List<TraceFilter> getTraceFilters() {
        return filters;
    }

}
