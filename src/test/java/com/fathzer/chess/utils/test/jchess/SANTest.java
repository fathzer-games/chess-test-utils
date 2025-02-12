package com.fathzer.chess.utils.test.jchess;

import com.fathzer.chess.utils.test.AbstractSANTest;
import com.fathzer.jchess.Move;
import com.fathzer.jchess.pgn.MoveAlgebraicNotationBuilder;

public class SANTest extends AbstractSANTest<JChessBoard, Move> {
	private static final MoveAlgebraicNotationBuilder builder;
	
	static {
		builder = new MoveAlgebraicNotationBuilder();
		builder.withEnPassantSymbol("");
	}

	@Override
	protected SANConverter<JChessBoard, Move> getSANConverter() {
		return (m,b) -> builder.get(b.getBoard(), m);
	}
}
