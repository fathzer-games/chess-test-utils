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
	 * <br>By default, this method returns true if the move is a move returned by {@link #getMoves()} and @link #makeMove(Object)} returns true.
	 * <br><b>Warning</b>: this method requires that the <code>equals</code> method is overridden to return true for moves are equivalent.
	 * @param move The move to check
	 * @return true if the move is legal, false otherwise
	 */
	default boolean isLegal(M move) {
		final boolean pseudoLegal = getMoves().stream().anyMatch(m -> m.equals(move));
		if (pseudoLegal) {
			final boolean result = makeMove(move);
			if (result) {
				unmakeMove();
			}
			return result;
		} else {
			return false;
		}
	}
}
