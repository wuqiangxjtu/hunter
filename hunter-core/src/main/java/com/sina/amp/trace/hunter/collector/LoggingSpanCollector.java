package com.sina.amp.trace.hunter.collector;

import static com.github.kristofa.brave.internal.Util.checkNotBlank;
import static com.github.kristofa.brave.internal.Util.checkNotNull;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.LinkedHashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kristofa.brave.SpanCollector;
import com.twitter.zipkin.gen.AnnotationType;
import com.twitter.zipkin.gen.BinaryAnnotation;
import com.twitter.zipkin.gen.Span;

/**
 * 这里只是把brave包里的LoggingSpanCollector的代码拿出来了，便于控制
 * @author wuqiang
 *
 */
public class LoggingSpanCollector implements SpanCollector{
	
    private static final String UTF_8 = "UTF-8";

    private final Logger logger;
    private final Set<BinaryAnnotation> defaultAnnotations = new LinkedHashSet();

    public LoggingSpanCollector() {
        logger = LoggerFactory.getLogger(LoggingSpanCollector.class);
    }

    public LoggingSpanCollector(String loggerName) {
        checkNotBlank(loggerName, "Null or blank loggerName");
        logger = LoggerFactory.getLogger(loggerName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void collect(final Span span) {
        checkNotNull(span, "Null span");
        if (!defaultAnnotations.isEmpty()) {
            for (final BinaryAnnotation ba : defaultAnnotations) {
                span.addToBinary_annotations(ba);
            }
        }

        if (getLogger().isInfoEnabled()) {
            getLogger().info(span.toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        // Nothing to do for this collector.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDefaultAnnotation(final String key, final String value) {
        checkNotBlank(key, "Null or blank key");
        checkNotNull(value, "Null value");

        try {
            final ByteBuffer bb = ByteBuffer.wrap(value.getBytes(UTF_8));

            final BinaryAnnotation binaryAnnotation = new BinaryAnnotation();
            binaryAnnotation.setKey(key);
            binaryAnnotation.setValue(bb);
            binaryAnnotation.setAnnotation_type(AnnotationType.STRING);
            defaultAnnotations.add(binaryAnnotation);

        } catch (final UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    Logger getLogger() {
        return logger;
    }
	
	

}
