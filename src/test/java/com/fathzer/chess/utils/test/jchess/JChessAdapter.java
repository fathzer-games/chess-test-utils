package com.fathzer.chess.utils.test.jchess;

import com.fathzer.chess.utils.model.TestAdapter;
import com.fathzer.chess.utils.model.Variant;
import com.fathzer.chess.utils.test.helper.Supports;
import com.fathzer.jchess.CoordinatesSystem;
import com.fathzer.jchess.Move;
import com.fathzer.jchess.Piece;
import com.fathzer.jchess.fen.FENParser;
import com.fathzer.jchess.generic.BasicMove;

@Supports(Variant.CHESS960)
public class JChessAdapter implements TestAdapter<JChessBoard, Move> {

	@Override
	public JChessBoard fenToBoard(String fen, Variant variant) {
		return new JChessBoard(new FENParser(fen).get());
	}

	@Override
	public Move move(JChessBoard board, String uciMove) {
		final CoordinatesSystem cs = board.getBoard().getCoordinatesSystem();
		final int from = cs.getIndex(uciMove.substring(0, 2));
		final int to = cs.getIndex(uciMove.substring(2, 4));
		final String promotion = uciMove.length()>4 ? uciMove.substring(4, 5) : null;
		final Piece piece;
		if (promotion!=null) {
			// Warning the promotion code is always in lowercase in UCI
			final String notation = board.getBoard().isWhiteToMove() ? promotion.toUpperCase() : promotion;
			piece = Piece.ALL.stream().filter(p -> notation.equals(p.getNotation())).findAny().orElse(null);
		} else {
			piece = null;
		}
		return new BasicMove(from, to, piece);
	}
}
