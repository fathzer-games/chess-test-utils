package com.fathzer.chess.utils.model;

/** An adapter class between the tested library's model and the model of the chess-test-utils library.
 * @param <B> the type of the board
 * @param <M> the type of the move
 */
public interface TestAdapter<B extends IBoard<M>, M> {
	/** Creates a board from a <a href="https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation">FEN</a> string.
	 * @param fen the FEN string
	 * @param variant the variant of the board
	 * @return the board created from the FEN string
	 */
	B fenToBoard(String fen, Variant variant);
}
