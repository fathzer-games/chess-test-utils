package com.fathzer.chess.utils.test.jchess;

import com.fathzer.jchess.Board;
import com.fathzer.jchess.CoordinatesSystem;
import com.fathzer.jchess.Move;
import com.fathzer.jchess.generic.BasicMove;

class JChessMove extends BasicMove {
	private String uci;

	JChessMove(Move move, Board<Move> board) {
		super(move.getFrom(), move.getTo(), move.getPromotion());
		final CoordinatesSystem coords = board.getCoordinatesSystem();
		uci = coords.getAlgebraicNotation(move.getFrom())+coords.getAlgebraicNotation(move.getTo());
		if (move.getPromotion() != null) {
			uci += move.getPromotion().getNotation().toLowerCase();
		}
	}
	
	@Override
	public String toString() {
		return uci;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof JChessMove) {
			return uci.equals(((JChessMove) obj).uci);
		}
		return false;
	}
}