package com.sina.amp.trace.hunter.spring.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.github.kristofa.brave.FixedSampleRateTraceFilter;
import com.github.kristofa.brave.TraceFilter;
import com.sina.amp.trace.hunter.TraceFilters;

@Configuration
public class TraceFiltersConfiguration {

    @Bean
    @Scope(value = "singleton")
    public TraceFilters traceFilters() {
        // Sample rate = 1 means every request will get traced.
        return new TraceFilters(Arrays.<TraceFilter>asList(new FixedSampleRateTraceFilter(1)));
    }

}
