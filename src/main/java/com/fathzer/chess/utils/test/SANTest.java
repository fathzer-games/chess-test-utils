package com.fathzer.chess.utils.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;

import static com.fathzer.chess.utils.model.Variant.*;

import org.junit.jupiter.api.Test;

import com.fathzer.chess.utils.model.IBoard;
import com.fathzer.chess.utils.model.Variant;

/** A test class for move to [Standard Algebraic Notation (SAN)](https://en.wikipedia.org/wiki/Algebraic_notation_(chess)) notation converter.
 * <br> Please note that there are many variants of the SAN standard, this test class uses the variant used in the PGN standard (for instance no <i>'e.p.'</i> for en passant captures).
 * Currently only this variant is addressed and supported.
 * @param <B> the type of the board
 * @param <M> the type of the move
*/
@Requires(SANTest.SANConverter.class)
public class SANTest<B extends IBoard<M>, M> extends AbstractAdaptableTest<B, M> {
	
	/** A class that converts a move to its Standard Algebraic Notation (SAN).
	 * @param <B> the type of the board
	 * @param <M> the type of the move
	 */
	@FunctionalInterface
	public interface SANConverter<B, M> {
		/** Converts a move to its Standard Algebraic Notation (SAN).
		 * @param board the board the move is played on
		 * @param move the move to convert
		 * @return the Standard Algebraic Notation (SAN) of the move
		*/
		String getSAN(B board, M move);
	}

	/** Gets the SAN converter to test.
	 * @return a SAN converter
	 */
	@SuppressWarnings("unchecked")
	protected SANConverter<B, M> getSANConverter() {
		return (SANConverter<B, M>)adapter;
	}

	private void testSAN(String fen, String uciMove, String expectedSan) {
		testSAN(fen, STANDARD, uciMove, expectedSan);
	}

	private void testSAN(String fen, Variant variant, String uciMove, String expectedSan) {
		final SANConverter<B, M> converter = getSANConverter();
		final B board = adapter.fenToBoard(fen, variant);
		assertEquals(expectedSan, converter.getSAN(board, board.toMove(uciMove)));
	}

	@Test
	@Tag("SANTest.pawnCatch")
	void pawnCatch() {
		testSAN("rnbqkbnr/pppp1ppp/8/4p3/3P3P/8/PPP1PPP1/RNBQKBNR b KQkq d3 0 2", "e5d4", "exd4");
	}

	@Test
	@Tag("SANTest.queenCatchWithCheck")
	void queenCatchWithCheck() {
		testSAN("r1b1k2r/ppp2ppp/5n2/7P/Pq1n4/6P1/1P1Q1P2/1R2KBNR b Kkq - 1 13", "b4d2", "Qxd2+");
	}

	@Test
	@Tag("SANTest.checkMate")
	void checkMate() {
		testSAN("r5k1/pp3ppp/2p2n2/P5PP/KP3P2/2r5/8/1bq5 b - - 0 28", "c1a3", "Qa3#");
	}

	@Test
	@Tag("SANTest.queenSideCastle")
	void queenSideCastle() {
		testSAN("r3k2r/pppnqppp/3b1n2/5b2/8/4P3/PPP2PPP/RNBQKBNR b KQkq - 3 6", "e8g8", "O-O");
	}

	@Test
	@Tag("SANTest.kingSideCastle")
	void kingSideCastle() {
		testSAN("r3k2r/pppnqppp/3b1n2/5b2/8/4P3/PPP2PPP/RNBQKBNR b KQkq - 3 6", "e8c8", "O-O-O");
	}

	@Test
	@Tag("SANTest.enPassant")
	void enPassant() {
		testSAN("rnbqkbnr/p1pppppp/8/PpP5/8/8/1P1PPPPP/RNBQKBNR w KQkq b6 0 1", "a5b6", "axb6");
	}

	@Test
	@Tag("SANTest.promotion")
	void promotion() {
		testSAN("2k1r3/Ppp1pP2/3p4/8/2P5/1P3K2/2PP2P1/R1B1Q3 w - - 0 1", "f7f8b", "f8=B");
	}

	@Test
	@Tag("SANTest.promotionWithCaptureAndCheckMate")
	void promotionWithCaptureAndCheckMate() {
		testSAN("2k1r3/Ppp1pP2/3p4/8/2P5/1P3K2/2PP2P1/R1B1Q3 w - - 0 1", "f7e8q", "fxe8=Q#");
	}

	@Test
	@Tag("SANTest.promotionWithCaptureAndStaleMate")
	void promotionWithCaptureAndStaleMate() {
		testSAN("4r3/2k1pP2/2p1P3/P1p1P3/2P5/5K2/2PP2P1/RQB5 w - - 0 1", "f7e8q", "fxe8=Q");
	}

	@Test
	@Tag("SANTest.draw")
	void draw() {
		testSAN("4k3/8/8/8/8/8/r5q1/4K3 b - - 0 1", "a2d2", "Rd2");
	}

	@Test
	@Tag("SANTest.verticalRooksAmbiguity")
	void verticalRooksAmbiguity() {
		testSAN("2kr3r/pppppppp/8/R7/2P1Q2Q/1P3K2/2PP2PP/RNB4Q w - - 0 1", "a1a3", "R1a3");
	}

	@Test
	@Tag("SANTest.horizontalRooksAmbiguity")
	void horizontalRooksAmbiguity() {
		testSAN("2kr3r/pppppppp/8/R7/2P1Q3/1P3K2/2PP2PP/RNB1Q2Q b - - 0 1", "d8f8", "Rdf8");
	}

	@Test
	@Tag("SANTest.threeQueensAmbiguity")
	void threeQueensAmbiguity() {
		testSAN("2kr3r/pppppppp/8/R7/2P1Q2Q/1P3K2/2PP2PP/RNB4Q w - - 0 1", "h4e1", "Qh4e1");
	}

	@Test
	@IfVariantSupported(Variant.CHESS960)
	@Tag("Chess960")
	@Tag("SANTest.chess960Castling")
	void chess960Castling() {
		testSAN("nrk1brnb/pp1ppppp/1q6/2p5/3P4/1NBQ1N2/1PP1PPPP/1RK2R1B w KQkq - 2 10", CHESS960, "c1b1", "O-O-O");
		testSAN("nrk1bbnr/pp1ppppp/1q6/2p5/3P4/1NBQ1NB1/1PP1PPPP/1RK4R w KQkq - 2 10", CHESS960, "c1h1","O-O");
	}
}
