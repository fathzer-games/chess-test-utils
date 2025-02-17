package com.fathzer.chess.utils.test;

import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import com.fathzer.chess.utils.model.IBoard;
import com.fathzer.chess.utils.model.TestAdapter;

class AdapterLoader {
	private static final String PROPERTY = "chess-test-utils.adapter";
	
	private TestAdapter<?, ?> adapter;
	private IllegalStateException serviceLoadingException;
	
	private boolean loadFromSystemProperty() {
        final String className = System.getProperty(PROPERTY);
        if (className != null) {
            try {
                adapter = (TestAdapter<?, ?>) Class.forName(className).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                serviceLoadingException = new IllegalStateException(String.format("Misconfiguration exception. Unable to load the adapter from the %s system property",PROPERTY), e);
            }
        }
        return className != null;
	}
	
	private void loadFromService() {
		try {
			adapter = ServiceLoader.load(TestAdapter.class).findFirst().orElse(null);
		} catch (ServiceConfigurationError e) {
			serviceLoadingException = new IllegalStateException("Misconfiguration exception. Unable to load the adapter from the service loader", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <B extends IBoard<M>, M> TestAdapter<B, M> get() {
		if (adapter == null && serviceLoadingException == null) {
			if (!loadFromSystemProperty()) {
				loadFromService();
			}
		}
		if (serviceLoadingException != null) {
			throw serviceLoadingException;
		}
		return (TestAdapter<B, M>) adapter;
	}
}
