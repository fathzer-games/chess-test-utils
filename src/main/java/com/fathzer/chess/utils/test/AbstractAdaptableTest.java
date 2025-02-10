package com.fathzer.chess.utils.test;

import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;

import com.fathzer.chess.utils.model.IBoard;
import com.fathzer.chess.utils.model.TestAdapter;

/** An abstract test class that requires a {@link TestAdapter}.
 * @param <B> the type of the board
 * @param <M> the type of the move
 */
public abstract class AbstractAdaptableTest<B extends IBoard<M>, M> implements Supplier<TestAdapter<B, M>> {
    /** The standard start position expressed in FEN */
	public static final String STANDARD_START_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
	
    /** The test adapter to use for the tests */
    protected TestAdapter<B, M> u;

    /** Set up  the test class
     * <br>By default, this method calls {@link #get()} and stores the result in the {@link #u} field.
     */
    @BeforeEach
    protected void setUp() {
        u = get();
    }
}
