package com.fathzer.chess.utils.model;

import java.util.List;

/** An interface representing a board.
 * @param <M> the type of the move
 */
public interface IBoard<M> {
	/** A constant that represents no piece */
	public static int NONE = 0;
	/** A constant that represents a pawn */
	public static int PAWN = 1;
	/** A constant that represents a knight */
	public static int KNIGHT = 2;
	/** A constant that represents a bishop */
	public static int BISHOP = 3;
	/** A constant that represents a rook */
	public static int ROOK = 4;
	/** A constant that represents a queen */
	public static int QUEEN = 5;
	/** A constant that represents a king */
	public static int KING = 6;
	
	
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
	
	/** Gets the piece at the given square.
	 * @param algebraicNotation the <a href="https://en.wikipedia.org/wiki/Algebraic_notation_(chess)">algebraic notation</a> of the square to get the piece from
	 * @return the piece at the given square (expressed with this interface constants). A positive number is returned for white pieces, a negative number for black pieces.
	 */
	int getPiece(String algebraicNotation);

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
