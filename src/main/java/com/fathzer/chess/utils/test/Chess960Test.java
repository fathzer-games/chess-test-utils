package com.fathzer.chess.utils.test;

import static org.junit.jupiter.api.Assertions.*;

import static com.fathzer.chess.utils.model.BoardPieceScanner.*;
import static com.fathzer.chess.utils.model.Variant.CHESS960;

import org.junit.jupiter.api.*;

import com.fathzer.chess.utils.model.BoardPieceScanner;
import com.fathzer.chess.utils.model.IBoard;
import com.fathzer.chess.utils.test.helper.Requires;

/** An abstract test class for <a href="https://en.wikipedia.org/wiki/Chess960">Chess960</a> variant.
 * @param <B> the type of the board
 * @param <M> the type of the move
 */
@Requires(BoardPieceScanner.class)
public class Chess960Test<B extends IBoard<M>, M> extends AbstractAdaptableTest<B, M> {
	// Most of the tests in this class are an adaptation of com.kelseyde.calvin.movegen.Chess960Test in https://github.com/kelseyde/calvin-chess-engine/
	
	@Test
	@Tag("Chess960Test.rookGoesToKingSquare")
	void rookGoesToKingSquare() {
		B board = adapter.fenToBoard("qnnbrk1r/ppppp1pp/5p2/3b4/3B4/5P2/PPPPP1PP/QNNBRK1R w KQkq - 2 3", CHESS960);
		M target = adapter.move(board, "f1h1");
		assertTrue(board.isLegal(target));
		board.makeMove(target);
		assertKingAndRook(board, "f1", "g1", "h1", "f1", true);
		board.unmakeMove();
		assertKingAndRook(board, "g1", "f1", "f1", "h1", true);
	}

	@Test
	@Tag("Chess960Test.rookGoesToKingSquareBlocked")
	void rookGoesToKingSquareBlocked() {
		B board = adapter.fenToBoard("qnnbrkbr/pppppppp/8/8/8/8/PPPPPPPP/QNNBRKBR w KQkq - 0 1", CHESS960);
		M target = adapter.move(board, "f1h1");
		assertFalse(board.isLegal(target));
	}

	@Test
	@Tag("Chess960Test.queensideRookOnKingside")
	void queensideRookOnKingside() {
		B board = adapter.fenToBoard("qn2rkbr/ppbppppp/1np5/8/8/1NP5/PPBPPPPP/QN2RKBR w KQkq - 2 4", CHESS960);
		M target = adapter.move(board, "f1e1");
		assertTrue(board.isLegal(target));
		board.makeMove(target);
		assertKingAndRook(board, "f1", "c1", "e1", "d1", true);
		board.unmakeMove();
		assertKingAndRook(board, "c1", "f1", "d1", "e1", true);
	}

	@Test
	@Tag("Chess960Test.queensideRookOnKingsideTwo")
	void queensideRookOnKingsideTwo() {
		B board = adapter.fenToBoard("bbqnr1nQ/1ppppp1p/8/p7/5k2/PP2N3/2PPPP1P/1B2RKNR w KQ - 2 8", CHESS960);
		M target = adapter.move(board, "f1e1");
		assertTrue(board.isLegal(target));
		board.makeMove(target);
		assertKingAndRook(board, "f1", "c1", "e1", "d1", true);
		board.unmakeMove();
		assertKingAndRook(board, "c1", "f1", "d1", "e1", true);
	}

	@Test
	@Tag("Chess960Test.queensideRookOnKingsideCastleKingside")
	void queensideRookOnKingsideCastleKingside() {
		B board = adapter.fenToBoard("bnqbrk1r/pppppppp/5n2/8/8/5N2/PPPPPPPP/BNQBRK1R w KQkq - 2 2", CHESS960);
		M target = adapter.move(board, "f1h1");
		assertTrue(board.isLegal(target));
		board.makeMove(target);
		assertKingAndRook(board, "f1", "g1", "h1", "f1", true);
		board.unmakeMove();
		assertKingAndRook(board, "g1", "f1", "f1", "h1", true);
	}

	@Test
	@Tag("Chess960Test.queensideRookOnKingsideBlocked")
	void queensideRookOnKingsideBlocked() {
		B board = adapter.fenToBoard("nbb1rkrn/pp1ppppp/1qp5/8/8/1QP5/PP1PPPPP/NBB1RKRN w KQkq - 2 3", CHESS960);
		M target = adapter.move(board, "f1e1");
		assertFalse(board.isLegal(target));
	}

	@Test
	@Tag("Chess960Test.kingsideRookOnQueenside")
	void kingsideRookOnQueenside() {
		B board = adapter.fenToBoard("nbbqr2n/ppp2kr1/3ppppp/8/8/3PPPN1/PPPBBQPP/NRKR4 w KQ - 2 8", CHESS960);
		M target = adapter.move(board, "c1d1");
		assertTrue(board.isLegal(target));
		board.makeMove(target);
		assertKingAndRook(board, "c1", "g1", "d1", "f1", true);
		board.unmakeMove();
		assertKingAndRook(board, "g1", "c1", "f1", "d1", true);
	}

	@Test
	@Tag("Chess960Test.kingsideRookOnQueensideBlocked")
	void kingsideRookOnQueensideBlocked() {
		B board = adapter.fenToBoard("nbbqr1rn/ppp2k2/3ppppp/8/8/3PPPN1/PPPBB1PP/NRKR2Q1 w KQ - 0 7", CHESS960);
		M target = adapter.move(board, "c1d1");
		assertFalse(board.isLegal(target));
	}

	@Test
	@Tag("Chess960Test.dontGetConfusedBetweenKingsideQueenside")
	void dontGetConfusedBetweenKingsideQueenside() {
		B board = adapter.fenToBoard("bqnbrk1r/pppppppp/5n2/8/8/5N2/PPPPPPPP/BQNBRK1R w KQkq - 2 2", CHESS960);
		M kingside = adapter.move(board, "f1h1");
		M queenside = adapter.move(board, "f1e1");
		assertTrue(board.isLegal(kingside));
		assertFalse(board.isLegal(queenside));
	}

	@Test
	@Tag("Chess960Test.kingDoesntMoveCastling")
	void kingDoesntMoveCastling() {
		// ... and rook is attacked, but the castling is legal
		final B board = adapter.fenToBoard("nrk2rnb/pp1ppppp/6b1/q1p5/3P2Q1/1N3N2/1P2PPPP/1RK1BR1B w KQkq - 2 10", CHESS960);
		final M move = adapter.move(board, "c1b1");
		assertTrue(board.isLegal(move));
		board.makeMove(move);
		assertKingAndRook(board, "c1", "c1", "b1", "d1", true);
	}
	
	@Test
	@Tag("Chess960Test.rookDoesntMoveCastling")
	void rookDoesntMoveCastling() {
		final B board = adapter.fenToBoard("nrk1brnb/pp1ppppp/1q6/2p5/3P4/1NBQ1N2/1PP1PPPP/1RK2R1B w KQkq - 2 10", CHESS960);
		final M move = adapter.move(board, "c1f1");
		assertTrue(board.isLegal(move));
		board.makeMove(move);
		assertKingAndRook(board, "c1", "g1", "f1", "f1", true);
	}

	@Test
	@Tag("Chess960Test.pinnedRookCastling")
	void pinnedRookCastling() {
		// Test castling where king seems safe ... but is not because it does not move and the rook does not defend it anymore
		final B board = adapter.fenToBoard("nrk1brnb/pp1ppppp/8/2p5/3P4/1N1Q1N2/1PP1PPPP/qRK1BR1B w KQkq - 2 10", CHESS960);
		final M move = adapter.move(board, "c1b1");
		assertFalse(board.isLegal(move));
	}

	@Test
	@Tag("Chess960Test.castlingOnStandardStartPosition")
	void castlingOnStandardStartPosition() {
		// Test that library is not misleading on castling move representation when board is in standard position
		// (which is one of the 960 possible chess960 startpositions)
		final B board = adapter.fenToBoard("rnbqk2r/pppp1ppp/3b1n2/4p3/4P3/3B1N2/PPPP1PPP/RNBQK2R w KQkq - 0 1", CHESS960);
		final M move = adapter.move(board, "e1h1");	
		assertTrue(board.isLegal(move));
	}

	private void assertKingAndRook(B board, String kingFrom, String kingTo, String rookFrom, String rookTo, boolean white) {
		final int kingToPiece = piece(board, kingTo);
		assertEquals(KING, Math.abs(kingToPiece));
		assertEquals(white, kingToPiece > 0);
		final int rookToPiece = piece(board, rookTo);
		assertEquals(ROOK, Math.abs(rookToPiece));
		assertEquals(white, rookToPiece > 0);
		if (!kingFrom.equals(kingTo) && !kingFrom.equals(rookTo)) {
			assertEquals(NONE, piece(board, kingFrom));
		}
		if (!rookFrom.equals(kingTo) && !rookFrom.equals(rookTo)) {
			assertEquals(NONE, piece(board, rookFrom));
		}
	}
	
	@SuppressWarnings("unchecked")
	private int piece(B board, String square) {
		return ((BoardPieceScanner<B>)adapter).getPiece(board, square);
	}
}
