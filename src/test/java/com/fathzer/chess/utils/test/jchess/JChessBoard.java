package com.fathzer.chess.utils.test.jchess;

import java.util.ArrayList;
import java.util.List;

import com.fathzer.chess.utils.model.IBoard;
import com.fathzer.games.GameHistory;
import com.fathzer.games.MoveGenerator.MoveConfidence;
import com.fathzer.jchess.Board;
import com.fathzer.jchess.Move;

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
	public String toString() {
		return board.toString();
	}
}
