package com.fathzer.chess.utils.test.chesslib;

import java.util.List;

import com.fathzer.chess.utils.model.IBoard;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;

public class ChessLibBoard implements IBoard<Move>{
	private final Board board;

	public ChessLibBoard(Board board) {
		this.board = board;
	}
	
	@Override
	public List<Move> getMoves() {
		return board.pseudoLegalMoves();
	}

	@Override
	public boolean makeMove(Move mv) {
		return board.doMove(mv);
	}

	@Override
	public void unmakeMove() {
		board.undoMove();
	}

	public Board getBoard() {
		return board;
	}
}
