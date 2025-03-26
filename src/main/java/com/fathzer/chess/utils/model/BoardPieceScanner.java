package com.fathzer.chess.utils.model;

/**
 * A class that can get the piece at a given square of a chess board.
 * 
 * @param <B> the type of the board
 */
@FunctionalInterface
public interface BoardPieceScanner<B extends IBoard<?>> {
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

	/** Gets the piece at the given square.
	 * @param board the board
	 * @param algebraicNotation the <a href="https://en.wikipedia.org/wiki/Algebraic_notation_(chess)">algebraic notation</a> of the square to get the piece from
	 * @return the piece at the given square (expressed with this interface constants). A positive number is returned for white pieces, a negative number for black pieces.
	 */
	int getPiece(B board, String algebraicNotation);
}
