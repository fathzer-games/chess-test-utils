package com.fathzer.chess.utils.test;

import static org.junit.jupiter.api.Assertions.*;

import static com.fathzer.chess.utils.model.Variant.CHESS960;
import static com.fathzer.chess.utils.model.IBoard.*;

import org.junit.jupiter.api.*;

import com.fathzer.chess.utils.model.IBoard;

/** An abstract test class for <a href="https://en.wikipedia.org/wiki/Chess960">Chess960</a> variant.
 * @param <B> the type of the board
 * @param <M> the type of the move
 */
public abstract class AbstractChess960Test<B extends IBoard<M>, M> extends AbstractAdaptableTest<B, M> {
	// Most of the tests in this class are an adaptation of com.kelseyde.calvin.movegen.Chess960Test in https://github.com/kelseyde/calvin-chess-engine/

    @Test
    void rookGoesToKingSquare() {
        B board = u.fenToBoard("qnnbrk1r/ppppp1pp/5p2/3b4/3B4/5P2/PPPPP1PP/QNNBRK1R w KQkq - 2 3", CHESS960);
        M target = u.move(board, "f1h1");
        assertTrue(board.isLegal(target));
        board.makeMove(target);
        assertKingAndRook(board, "f1", "g1", "h1", "f1", true);
        board.unmakeMove();
        assertKingAndRook(board, "g1", "f1", "f1", "h1", true);
    }

    @Test
    void rookGoesToKingSquareBlocked() {
        B board = u.fenToBoard("qnnbrkbr/pppppppp/8/8/8/8/PPPPPPPP/QNNBRKBR w KQkq - 0 1", CHESS960);
        M target = u.move(board, "f1h1");
        assertFalse(board.isLegal(target));
    }

    @Test
    void queensideRookOnKingside() {
        B board = u.fenToBoard("qn2rkbr/ppbppppp/1np5/8/8/1NP5/PPBPPPPP/QN2RKBR w KQkq - 2 4", CHESS960);
        M target = u.move(board, "f1e1");
        assertTrue(board.isLegal(target));
        board.makeMove(target);
        assertKingAndRook(board, "f1", "c1", "e1", "d1", true);
        board.unmakeMove();
        assertKingAndRook(board, "c1", "f1", "d1", "e1", true);
    }

    @Test
    void queensideRookOnKingsideTwo() {
        B board = u.fenToBoard("bbqnr1nQ/1ppppp1p/8/p7/5k2/PP2N3/2PPPP1P/1B2RKNR w KQ - 2 8", CHESS960);
        M target = u.move(board, "f1e1");
        assertTrue(board.isLegal(target));
        board.makeMove(target);
        assertKingAndRook(board, "f1", "c1", "e1", "d1", true);
        board.unmakeMove();
        assertKingAndRook(board, "c1", "f1", "d1", "e1", true);
    }

    @Test
    void queensideRookOnKingsideCastleKingside() {
        B board = u.fenToBoard("bnqbrk1r/pppppppp/5n2/8/8/5N2/PPPPPPPP/BNQBRK1R w KQkq - 2 2", CHESS960);
        M target = u.move(board, "f1h1");
        assertTrue(board.isLegal(target));
        board.makeMove(target);
        assertKingAndRook(board, "f1", "g1", "h1", "f1", true);
        board.unmakeMove();
        assertKingAndRook(board, "g1", "f1", "f1", "h1", true);
    }

    @Test
    void queensideRookOnKingsideBlocked() {
        B board = u.fenToBoard("nbb1rkrn/pp1ppppp/1qp5/8/8/1QP5/PP1PPPPP/NBB1RKRN w KQkq - 2 3", CHESS960);
        M target = u.move(board, "f1e1");
        assertFalse(board.isLegal(target));
    }

    @Test
    void kingsideRookOnQueenside() {
        B board = u.fenToBoard("nbbqr2n/ppp2kr1/3ppppp/8/8/3PPPN1/PPPBBQPP/NRKR4 w KQ - 2 8", CHESS960);
        M target = u.move(board, "c1d1");
        assertTrue(board.isLegal(target));
        board.makeMove(target);
        assertKingAndRook(board, "c1", "g1", "d1", "f1", true);
        board.unmakeMove();
        assertKingAndRook(board, "g1", "c1", "f1", "d1", true);
    }

    @Test
    void kingsideRookOnQueensideBlocked() {
        B board = u.fenToBoard("nbbqr1rn/ppp2k2/3ppppp/8/8/3PPPN1/PPPBB1PP/NRKR2Q1 w KQ - 0 7", CHESS960);
        M target = u.move(board, "c1d1");
        assertFalse(board.isLegal(target));
    }

    @Test
    void dontGetConfusedBetweenKingsideQueenside() {
        B board = u.fenToBoard("bqnbrk1r/pppppppp/5n2/8/8/5N2/PPPPPPPP/BQNBRK1R w KQkq - 2 2", CHESS960);
        M kingside = u.move(board, "f1h1");
        M queenside = u.move(board, "f1e1");
        assertTrue(board.isLegal(kingside));
        assertFalse(board.isLegal(queenside));
    }

	@Test
	void kingDoesntMoveCastling() {
		// Rook is attacked, but the castling is legal
		final B board = u.fenToBoard("nrk2rnb/pp1ppppp/6b1/q1p5/3P2Q1/1N3N2/1P2PPPP/1RK1BR1B w KQkq - 2 10", CHESS960);
		final M move = u.move(board, "c1b1");
		assertTrue(board.isLegal(move));
	}

	@Test
	void pinnedRookCastling() {
		// Test castling where king seems safe ... but is not because it does not move and the rook does not defend it anymore
		final B board = u.fenToBoard("nrk1brnb/pp1ppppp/8/2p5/3P4/1N1Q1N2/1PP1PPPP/qRK1BR1B w KQkq - 2 10", CHESS960);
		final M move = u.move(board, "c1b1");
		assertFalse(board.isLegal(move));
	}

    private void assertKingAndRook(B board, String kingFrom, String kingTo, String rookFrom, String rookTo, boolean white) {
        final int kingToPiece = board.getPiece(kingTo);
        assertEquals(KING, Math.abs(kingToPiece));
        assertEquals(white, kingToPiece > 0);
        final int rookToPiece = board.getPiece(rookTo);
        assertEquals(ROOK, Math.abs(rookToPiece));
        assertEquals(white, rookToPiece > 0);
        if (!kingFrom.equals(rookTo)) {
            assertEquals(NONE, board.getPiece(kingFrom));
        }
        if (!rookFrom.equals(kingTo)) {
            assertEquals(NONE, board.getPiece(rookFrom));
        }
    }
}
