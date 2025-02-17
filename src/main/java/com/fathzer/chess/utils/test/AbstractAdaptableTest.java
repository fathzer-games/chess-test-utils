package com.fathzer.chess.utils.test;

import static com.fathzer.chess.utils.model.Variant.STANDARD;

import java.util.Arrays;

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

	private static final AdapterLoader ADAPTER_LOADER = new AdapterLoader();
	
	/** The test adapter to use for the tests */
	protected TestAdapter<B, M> adapter;
	
	/** Creates a new instance.
	 * @throws IllegalStateException if the adapter can't be created. This adapter is created by calling {@link #getAdapter()}.
	 * @see #getAdapter()	
	 */
	protected AbstractAdaptableTest() {
		adapter = getAdapter();
		final Requires requires = getClass().getAnnotation(Requires.class);
		if (requires!=null) {
			for (Class<?> requiredInterface : requires.value()) {
				if (!requiredInterface.isAssignableFrom(adapter.getClass())) {
					throw new IllegalStateException(String.format("%s class requires that %s adapter implements %s)",getClass(), adapter.getClass(), requiredInterface));
				}
			}
		}
	}

	/** Gets the {@link TestAdapter} to use for this tests.
	 * <br>The default implementation loads the class whose name is in the `chess-test-utils.adapter` system property,
	 * if that property is set.
	 * <br>If it is not set, it loads the adapter from the service loader (The service loader take the adapter class name in
	 * the <code>META-INF/services/com.fathzer.chess.utils.model.TestAdapter</code> resource file).
	 * <br>You can override this method to provide a different way to load the adapter.
	 * @return the test adapter instance (never null)
	 * @throws IllegalStateException if the resource file is not found or if the service class can't be loaded.
	 */
	protected TestAdapter<B,M> getAdapter() {
		return ADAPTER_LOADER.get();
	}

	/** Checks if a variant is supported by the current adapter.
	 * <br>By default, this method returns true if variant is {@link Variant#STANDARD}, otherwise, it checks if the {@link TestAdapter} has
	 * a {@link Supports} annotation that contains the variant.
	 * @param variant The variant to test
	 * @return <code>true</code> if the variant is supported, <code>false</code> otherwise
	 */
	public boolean isSupported(Variant variant) {
		if (variant==STANDARD) return true;
		final Supports supports = adapter.getClass().getAnnotation(Supports.class);
		return supports!=null && Arrays.asList(supports.value()).contains(variant);
	}
}
