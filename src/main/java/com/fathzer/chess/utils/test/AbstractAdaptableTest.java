package com.fathzer.chess.utils.test;

import static com.fathzer.chess.utils.model.Variant.STANDARD;

import java.util.Arrays;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import com.fathzer.chess.utils.model.IBoard;
import com.fathzer.chess.utils.model.TestAdapter;
import com.fathzer.chess.utils.model.Variant;
import com.fathzer.chess.utils.test.helper.Requires;
import com.fathzer.chess.utils.test.helper.Supports;

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
	
	/** Creates a new instance.
	 * @throws IllegalStateException if the resource file is not found or if the service class can't be loaded.
	 */
	protected AbstractAdaptableTest() {
		u = getAdapter();
		final Requires requires = getClass().getAnnotation(Requires.class);
		if (requires!=null) {
			for (Class<?> requiredInterface : requires.value()) {
				if (!requiredInterface.isAssignableFrom(u.getClass())) {
					throw new IllegalStateException(String.format("%s class requires that %s adapter implements %s)",getClass(), u.getClass(), requiredInterface));
				}
			}
		}
	}

	/** Gets the {@link TestAdapter} to use for this tests.
	 * <br>It returns an instance of the service class whose name is in
	 * the <code>META-INF/services/com.fathzer.chess.utils.model.TestAdapter</code> resource file.
	 * @return the test adapter instance (never null)
	 * @throws IllegalStateException if the resource file is not found or if the service class can't be loaded.
	 */
	@SuppressWarnings("unchecked")
	private TestAdapter<B,M> getAdapter() {
		if (ADAPTER==null) {
			if (SERVICE_LOADING_EXCEPTION!=null) {
				throw new IllegalStateException("Misconfiguration exception. An error occurred while loading the "+SERVICE_RESOURCE+" resource service file", SERVICE_LOADING_EXCEPTION);
			}
			throw new IllegalStateException("Misconfiguration exception. You should provide a "+SERVICE_RESOURCE+" resource service file or override the getAdapter method");
		} else {
			return (TestAdapter<B, M>) ADAPTER;
		}
	}

	/** Checks if a variant is supported by the current adapter.
	 * <br>By default, this method returns true if variant is {@link Variant#STANDARD}, otherwise, it checks if the {@link TestAdapter} has
	 * a {@link Supports} annotation that contains the variant.
	 * @param variant The variant to test
	 * @return <code>true</code> if the variant is supported, <code>false</code> otherwise
	 */
	public boolean isSupported(Variant variant) {
		if (variant==STANDARD) return true;
		final Supports supports = u.getClass().getAnnotation(Supports.class);
		return supports!=null && Arrays.asList(supports.value()).contains(variant);
	}
}
