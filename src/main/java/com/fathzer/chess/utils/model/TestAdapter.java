package com.fathzer.chess.utils.model;

/** An adapter class between the tested library's model and the model of the chess-test-utils library.
 * @param <B> the type of the board
 * @param <M> the type of the move
 */
public interface TestAdapter<B extends IBoard<M>, M> {
	/** Creates a board from a FEN string.
	 * @param fen the FEN string
	 * @param variant the variant of the board
	 * @return the board created from the FEN string
	 */
	B fenToBoard(String fen, Variant variant);

	/** Converts the UCI representation of a move to a move instance.
	 * <br>There is no guarantee that this method is called only with legal moves.
	 * <br>If the move is illegal, the method should do its best to return a move instance
	 * representing the illegal move.
	 * @param board the board on which the move is played.
	 * @param uciMove the move in UCI format
	 * @return a move instance.
	 */
	M move(B board, String uciMove);
	
	/**
	 * Converts a move to its UCI representation.
	 * <br>The default implementation returns the move's {@link Object#toString() toString()} value.
	 * <br>Subclasses may override this method to return the move's UCI representation.
	 * @param board the board on which the move is played.
	 * @param move the move to convert
	 * @return the UCI representation of the move
	 */
	default String toUCI(B board, M move) {
		return move.toString();
	}
}
