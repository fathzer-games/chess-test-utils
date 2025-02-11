package com.fathzer.chess.utils.test;

import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import org.junit.jupiter.api.BeforeEach;

import com.fathzer.chess.utils.model.IBoard;
import com.fathzer.chess.utils.model.TestAdapter;

/** An abstract test class that requires a {@link TestAdapter}.
 * @param <B> the type of the board
 * @param <M> the type of the move
 */
public abstract class AbstractAdaptableTest<B extends IBoard<M>, M> {
    /** The standard start position expressed in FEN */
	public static final String STANDARD_START_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

	private static final TestAdapter<?,?> ADAPTER;
    private static final ServiceConfigurationError SERVICE_LOADING_EXCEPTION;
	private static final String SERVICE_RESOURCE = "META-INF/services/"+TestAdapter.class;
	
	static {
		TestAdapter<?,?> service = null;
        ServiceConfigurationError serviceLoadingException = null;
		try {
			service = ServiceLoader.load(TestAdapter.class).findFirst().orElse(null);
		} catch (ServiceConfigurationError e) {
            serviceLoadingException = e;
		}
		ADAPTER = service;
		SERVICE_LOADING_EXCEPTION = serviceLoadingException;
	}
	
    /** The test adapter to use for the tests */
    protected TestAdapter<B, M> u;

    /** Set up  the test class
     * <br>By default, this method calls {@link #getAdapter()} and stores the result in the {@link #u} field.
     */
    @BeforeEach
    protected void setUp() {
        u = getAdapter();
    }
    
    /** Gets the {@link TestAdapter} to use for this tests.
     * <br>Default implementation returns an instance of the service class whose name is in
     * the <code>META-INF/services/com.fathzer.chess.utils.model.TestAdapter</code> resource file.
     * <br>If you prefer not to rely on service files, override this method.
     * @return the test adapter instance (never null)
     * @throws IllegalStateException if the resource file is not found or if the service class can't be loaded.
     */
    @SuppressWarnings("unchecked")
	protected TestAdapter<B,M> getAdapter() {
    	if (ADAPTER==null) {
            if (SERVICE_LOADING_EXCEPTION!=null) {
                throw new IllegalStateException("Misconfiguration exception. An error occurred while loading the "+SERVICE_RESOURCE+" resource service file", SERVICE_LOADING_EXCEPTION);
            }
        	throw new IllegalStateException("Misconfiguration exception. You should provide a "+SERVICE_RESOURCE+" resource service file or override the getAdapter method");
    	} else {
    		return (TestAdapter<B, M>) ADAPTER;
    	}
    }
}
