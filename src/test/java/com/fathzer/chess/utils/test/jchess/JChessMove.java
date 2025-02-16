package com.fathzer.chess.utils.test.jchess;

import com.fathzer.jchess.Board;
import com.fathzer.jchess.CoordinatesSystem;
import com.fathzer.jchess.Move;

class JChessMove {
	Move mv;
	private Board<Move> board;

	JChessMove(Move move, Board<Move> board) {
		this.mv = move;
		this.board = board;
	}
	
	@Override
	public String toString() {
		final CoordinatesSystem coords = board.getCoordinatesSystem();
		return mv.toString(coords).replace("-", "");
	}
}