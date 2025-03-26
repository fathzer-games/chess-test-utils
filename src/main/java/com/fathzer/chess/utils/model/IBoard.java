package com.fathzer.chess.utils.model;

import java.util.List;

/** An interface representing a board.
 * @param <M> the type of the move
 */
public interface IBoard<M> {
	
	/** Gets the list of moves available on the board.
	 * <br>The board is free to return legal or <a href="https://www.chessprogramming.org/Pseudo-Legal_Move">pseudo legal</a> moves.
	 * @return the list of moves
	 */
	List<M> getMoves();
	
	/** Returns true if this IBoard's getMoves() method returns legal moves.
	 * @return true if this IBoard's getMoves() method returns legal moves, false (the default value) otherwise (Typically, if it returns <a href="https://www.chessprogramming.org/Pseudo-Legal_Move">pseudo legal moves</a>)
	 */
	default boolean isGetMovesLegal() {
		return false;
	}
	
	/** Converts the UCI representation of a move to a move instance.
	 * <br>By default, this method returns a move instance from the list returned by {@link #getMoves()}. If the move is not in the list, an {@link IllegalArgumentException} is thrown.
	 * <br>There is no guarantee that this method is called only with legal or pseudo-legal moves.
	 * <br>If the move is illegal, the method should throw an {@link IllegalArgumentException}.
	 * @param uciMove the move in UCI format
	 * @return a pseudo legal (or legal, see {@link #isGetMovesLegal()}) move instance.
	 * @throws IllegalArgumentException if move is not legal
	 */
	default M toMove(String uciMove) {
		return getMoves().stream().filter(m -> toUCI(m).equals(uciMove)).findFirst().orElseThrow(() -> new IllegalArgumentException("Move " + uciMove + " is not legal"));
	}
	
	/**
	 * Converts a move to its UCI representation.
	 * <br>The default implementation returns the move's {@link Object#toString() toString()} value.
	 * <br>Subclasses must override this method to return the move's UCI representation.
	 * @param move the move to convert
	 * @return the UCI representation of the move
	 */
	default String toUCI(M move) {
		return move.toString();
	}

	/** Makes a move on the board.
	 * <br>It is guaranteed that this method is called only with legal moves or moves returned by {@link #getMoves()}.
	 * @param mv the move to make
	 * @return true if the move was legal, false otherwise. If the move was illegal, the board is not modified.
	 */
	boolean makeMove(M mv);

	/** Undoes the last move made on the board.
	 */
	void unmakeMove();
	
	/** Checks if the given move is legal.
	 * <br>By default, this method returns true if {@link #isGetMovesLegal()} returns true or if {@link #makeMove(Object)} returns true.
	 * @param move The move to check
	 * @return true if the move is legal, false otherwise
	 */
	default boolean isLegal(M move) {
		if (isGetMovesLegal()) {
			return true;
		} else {
			final boolean result = makeMove(move);
			if (result) {
				unmakeMove();
			}
			return result;
		}
	}
	
	/** Checks if the given uci move is legal.
	 * <br>By default, this method returns true if the move is a move returned by {@link #getMoves()} and @link #makeMove(Object)} returns true.
	 * <br><b>Warning</b>: this method requires that the <code>equals</code> method is overridden to return true for moves are equivalent.
	 * @param uci The move to check
	 * @return true if the move is legal, false otherwise
	 */
	default boolean isLegal(String uci) {
		final M move;
		try {
			move = toMove(uci);
		} catch (IllegalArgumentException e) {
			return false;
		}
		return isLegal(move);
	}
}
