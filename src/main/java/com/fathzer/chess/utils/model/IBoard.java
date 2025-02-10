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

	/** Makes a move on the board.
	 * <br>It is guaranteed that this method is called only with legal moves or moves returned by {@link #getMoves()}.
	 * @param mv the move to make
	 * @return true if the move was legal, false otherwise. If the move was illegal, the board is not modified.
	 */
	boolean makeMove(M mv);

	/** Undoes the last move made on the board.
	 */
	void unmakeMove();
    
}
