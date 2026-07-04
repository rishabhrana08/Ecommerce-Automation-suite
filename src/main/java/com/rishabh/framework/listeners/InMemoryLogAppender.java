package com.rishabh.framework.listeners;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import java.io.Serializable;

/**
 * Custom log4j2 appender that keeps a per-thread buffer of everything logged during
 * the current test, so TestListener can dump "what actually happened" into the Allure
 * report next to the screenshot instead of making someone go dig through execution.log.
 */
@Plugin(name = "InMemoryLogAppender", category = "Core", elementType = "appender", printObject = true)
public class InMemoryLogAppender extends AbstractAppender {

    private static final ThreadLocal<StringBuilder> BUFFER = ThreadLocal.withInitial(StringBuilder::new);

    protected InMemoryLogAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
        super(name, filter, layout, false, null);
    }

    @PluginFactory
    public static InMemoryLogAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginElement("Filter") Filter filter) {
        return new InMemoryLogAppender(name, filter, layout);
    }

    @Override
    public void append(LogEvent event) {
        BUFFER.get().append(new String(getLayout().toByteArray(event)));
    }

    public static String getLogsForCurrentTest() {
        return BUFFER.get().toString();
    }

    public static void clear() {
        BUFFER.get().setLength(0);
    }
}
