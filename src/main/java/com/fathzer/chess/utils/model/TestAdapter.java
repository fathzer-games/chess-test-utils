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

    /** Converts the UCI representation of a legal move to a move instance.
     * <br>It is guaranteed that this method is called by this API only with legal moves.
     * <br>So, the behavior is undefined if the move is not legal (you may throw an exception or not, as you wish).
     * @param board the board on which the move is played.
     * @param uciMove the move in UCI format
     * @return a move instance.
     */
    M legalMove(B board, String uciMove);

    /** Converts the UCI representation of an illegal move to a move instance.
     * @param uciMove the move in UCI format
     * @return a move instance
     */
    M move(String uciMove);

    /** Returns whether the adapter supports the given variant.
     * <br>By default, this method returns <code>true</code> if the variant is <code>STANDARD</code>.
     * @param variant the variant to check
     * @return <code>true</code> if the adapter supports the given variant, <code>false</code> otherwise
     */
    default boolean isSupported(Variant variant) {
        return variant==Variant.STANDARD;
    }
}
