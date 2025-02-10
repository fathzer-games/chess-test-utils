package com.fathzer.chess.utils.test;

import static org.junit.jupiter.api.Assertions.*;

import static com.fathzer.chess.utils.model.Variant.*;

import org.junit.jupiter.api.Test;

import com.fathzer.chess.utils.model.IBoard;

/** A test class for move to [Standard Algebraic Notation (SAN)](https://en.wikipedia.org/wiki/Algebraic_notation_(chess)) notation converter.
 * <br> Please note that there are many variants of the SAN standard, this test class uses the variant used in the PGN standard (for instance no <i>'e.p.'</i> for en passant captures).
 * Currently only this variant is addressed and supported.
 * @param <B> the type of the board
 * @param <M> the type of the move
*/
public abstract class AbstractSANTest<B extends IBoard<M>, M> extends AbstractAdaptableTest<B, M> {

    /** A class that converts a move to its Standard Algebraic Notation (SAN). */
    @FunctionalInterface
    public interface SANConverter<B, M> {
        /** Converts a move to its Standard Algebraic Notation (SAN).
         * @param move the move to convert
         * @param board the board the move is played on
         * @return the Standard Algebraic Notation (SAN) of the move
         * @throws IllegalArgumentException if the move is illegal.
         * <br>Note: Throwing an exception is the default behavior, but if your library prefers to return null,
         * or a SAN value, you can do so. In such a case, you should override the {@link #checkIllegalMove} method
         * accordingly with your library's behavior.
         * @see #checkIllegalMove
        */
        String getSAN(M move, B board);
    }

    /** Gets the SAN converter to test.
     * @return a SAN converter
     */
	protected abstract SANConverter<B, M> getSANConverter();

    /** Checks if illegal moves are handled correctly.
     * <br>The default implementation asserts an {@link IllegalArgumentException} is thrown if the move is illegal.
     * <br>Subclasses may override this method to change the behavior if your library handles illegal moves differently.
     * @param converter the test SAN converter
     * @param move the move to convert
     * @param board the board the move is played on
     */
    protected void checkIllegalMove(SANConverter<B, M> converter, M move, B board) {
        assertThrows(IllegalArgumentException.class, () -> converter.getSAN(move, board));
    }

    @Test
    void test() {
        final SANConverter<B, M> converter = getSANConverter();
        
        B board = u.fenToBoard("rnbqkbnr/pppp1ppp/8/4p3/3P3P/8/PPP1PPP1/RNBQKBNR b KQkq d3 0 2", STANDARD);
        assertEquals("exd4", converter.getSAN(u.legalMove(board, "e5d4"), board));
        
        board = u.fenToBoard("r1b1k2r/ppp2ppp/5n2/7P/Pq1n4/6P1/1P1Q1P2/1R2KBNR b Kkq - 1 13", STANDARD);
        assertEquals("Qxd2+", converter.getSAN(u.legalMove(board, "b4d2"), board));
        
        board = u.fenToBoard("r5k1/pp3ppp/2p2n2/P5PP/KP3P2/2r5/8/1bq5 b - - 0 28", STANDARD);
        assertEquals("Qa3#", converter.getSAN(u.legalMove(board, "c1a3"), board));
        
        board = u.fenToBoard("r3k2r/pppnqppp/3b1n2/5b2/8/4P3/PPP2PPP/RNBQKBNR b KQkq - 3 6", STANDARD);
        assertEquals("O-O", converter.getSAN(u.legalMove(board, "e8g8"), board));
        board = u.fenToBoard("r3k2r/pppnqppp/3b1n2/5b2/8/4P3/PPP2PPP/RNBQKBNR b KQkq - 3 6", STANDARD);
        assertEquals("O-O-O", converter.getSAN(u.legalMove(board, "e8c8"), board));
        
        board = u.fenToBoard("rnbqkbnr/p1pppppp/8/PpP5/8/8/1P1PPPPP/RNBQKBNR w KQkq b6 0 1", STANDARD);
        assertEquals("axb6", converter.getSAN(u.legalMove(board, "a5b6"), board));
        
        board = u.fenToBoard("2kr3r/Ppp1pppp/3p4/8/2P5/1P3K2/2PP2PP/R1B1Q3 w - - 0 1", STANDARD);
        assertEquals("a8=Q+",converter.getSAN(u.legalMove(board, "a7a8q"), board));
        
        board = u.fenToBoard("4k3/8/8/8/8/8/r5q1/4K3 b - - 0 1", STANDARD);
        assertEquals("Rd2",converter.getSAN(u.legalMove(board, "a2d2"), board),"Problem with DRAW");
        
        if (u.isSupported(CHESS960)) {
            // Castling in chess960
            board = u.fenToBoard("4k3/8/8/8/8/8/r5q1/4K3 b - - 0 1", CHESS960);
            //TODO
        }
        
        // Disambiguation
        board = u.fenToBoard("2kr3r/pppppppp/8/R7/2P1Q2Q/1P3K2/2PP2PP/RNB4Q w - - 0 1", STANDARD);
        assertEquals("R1a3", converter.getSAN(u.legalMove(board, "a1a3"), board));
        M mv = u.legalMove(board, "h4e1");
        assertEquals("Qh4e1", converter.getSAN(mv, board));
        board.makeMove(mv);
        mv = u.legalMove(board, "d8f8");
        assertEquals("Rdf8", converter.getSAN(u.legalMove(board, "d8f8"), board));
        
        // Illegal moves
        checkIllegalMove(converter, u.move("f3g2"), board);
        
        board = u.fenToBoard("2kr3r/Rppppppp/8/8/2P1Q2Q/1P3K2/2PP2PP/RNB4Q w - - 0 1", STANDARD);
        checkIllegalMove(converter, u.move("a7a8q"), board);
    }
}
