package com.fathzer.chess.utils.test;

import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;

import com.fathzer.chess.utils.model.IBoard;
import com.fathzer.chess.utils.model.TestAdapter;

public abstract class AbstractAdaptableTest<B extends IBoard<M>, M> implements Supplier<TestAdapter<B, M>> {
	public static final String STANDARD_START_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
	
    protected TestAdapter<B, M> u;

    @BeforeEach
    protected void setUp() {
        u = get();
    }

}
