package org.natty;

import java.util.ServiceLoader;

public interface EventSearcherProvider extends ServiceLoader.Provider<EventSearcher<?>> {
}
