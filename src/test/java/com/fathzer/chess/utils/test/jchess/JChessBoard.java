package com.fathzer.chess.utils.test.jchess;

import java.util.ArrayList;
import java.util.List;

import com.fathzer.chess.utils.model.IBoard;
import com.fathzer.games.Color;
import com.fathzer.games.GameHistory;
import com.fathzer.games.MoveGenerator.MoveConfidence;
import com.fathzer.jchess.Board;
import com.fathzer.jchess.Move;
import com.fathzer.jchess.Piece;

public class JChessBoard implements IBoard<JChessMove>{
	private final Board<Move> startBoard;
	public final Board<Move> board;
	private final List<Move> moves;

	public JChessBoard(Board<Move> board) {
		this.board = board;
		this.startBoard = (Board<Move>) board.fork();
		this.moves = new ArrayList<>(256);
	}
	
	@Override
	public List<JChessMove> getMoves() {
		return board.getMoves().stream().map(m -> new JChessMove(m, this.board)).toList();
	}

	@Override
	public boolean makeMove(JChessMove mv) {
		final boolean isValid = board.makeMove(mv, MoveConfidence.UNSAFE);
		if (isValid) {
			moves.add(mv);
		}
		return isValid;
	}

	@Override
	public void unmakeMove() {
		board.unmakeMove();
		moves.remove(moves.size()-1);
	}

	Board<Move> getBoard() {
		return board;
	}
	
	GameHistory<Move, Board<Move>> toHistory() {
		GameHistory<Move, Board<Move>> history = new GameHistory<>(startBoard);
		for (Move mv : moves) {
			history.add(mv);
		}
		return history;
	}

	@Override
	public int getPiece(String algebraicNotation) {
		final Piece piece = board.getPiece(board.getCoordinatesSystem().getIndex(algebraicNotation));
		if (piece==null) {
			return 0;
		} else {
			int result = (piece.ordinal()+1)/2;
			return Color.WHITE==piece.getColor() ? result : -result; 
		}
	}

	@Override
	public String toString() {
		return board.toString();
	}
}
