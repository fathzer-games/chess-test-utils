package com.fathzer.chess.utils.test.chesslib;

import com.fathzer.chess.utils.model.TestAdapter;
import com.fathzer.chess.utils.model.Variant;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;

public class ChessLibAdapter implements TestAdapter<ChessLibBoard, Move> {

	@Override
	public ChessLibBoard fenToBoard(String fen, Variant variant) {
		final Board internalBoard = new Board();
		internalBoard.loadFromFen(fen);
		return new ChessLibBoard(internalBoard);
	}

	@Override
	public Move move(ChessLibBoard board, String uciMove) {
		final String from = uciMove.substring(0, 2);
		final String to = uciMove.substring(2, 4);
		final String promotion = uciMove.length()>4 ? uciMove.substring(4, 5) : null;
		Piece p = null;
		if (promotion!=null) {
			// Warning the promotion code is always in lowercase in UCI
			final String notation = board.getBoard().getSideToMove()==Side.WHITE ? promotion.toUpperCase() : promotion;
			p = Piece.fromFenSymbol(notation);
		} else {
			p = Piece.NONE;
		}
		return new Move(Square.fromValue(from.toUpperCase()), Square.fromValue(to.toUpperCase()), p);
	}
}
