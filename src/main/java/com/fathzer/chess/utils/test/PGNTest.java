package com.fathzer.chess.utils.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fathzer.chess.utils.model.IBoard;
import com.fathzer.chess.utils.model.Variant;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/** A test class for <a href="https://en.wikipedia.org/wiki/Portable_Game_Notation">PGN</a> builder.
 * @param <B> the type of the board
 * @param <M> the type of the move
 */
@Requires(PGNTest.PGNConverter.class)
public class PGNTest<B extends IBoard<M>, M> extends AbstractAdaptableTest<B, M> {
	
	/** A converter from a board to its PGN representation
	 * @param <B> the type of the board
	 */
	@FunctionalInterface
	public interface PGNConverter<B> {
		/** Gets the PGN representation of the given board
		 * @param board the board
		 * @return the PGN representation of the given board
		*/
		String toPGN(B board);
	}
	
	/** The moves, in UCI format, of the famous Lasker vs Thomas, 1912 game */
	protected static final String FATAL_ATTRACTION_MOVES = "d2d4 e7e6 g1f3 f7f5 b1c3 g8f6 c1g5 f8e7 g5f6 e7f6 e2e4"
			+ " f5e4 c3e4 b7b6 f3e5 e8g8 f1d3 c8b7 d1h5 d8e7 h5h7 g8h7 e4f6 h7h6 e5g4 h6g5 h2h4 g5f4 g2g3 f4f3 d3e2"
			+ " f3g2 h1h2 g2g1 e1d2";

	/** The PGN event tag */
	protected static final String EVENT_TAG = "Event";

	/** The PGN site tag */
	protected static final String SITE_TAG = "Site";

	/** The PGN date tag */
	protected static final String DATE_TAG = "Date";

	/** The PGN round tag */
	protected static final String ROUND_TAG = "Round";

	/** The PGN white tag */
	protected static final String WHITE_TAG = "White";

	/** The PGN black tag */
	protected static final String BLACK_TAG = "Black";

	/** The PGN result tag */
	protected static final String RESULT_TAG = "Result";

	/** The PGN FEN tag */
	protected static final String FEN_TAG = "FEN";

	/** The PGN variant tag */
	protected static final String VARIANT_TAG = "Variant";

	 
	/** The seven tag roster keys in the order specified in the PGN specification (see <a href="https://github.com/fsmosca/PGN-Standard/blob/master/PGN-Standard.txt">PGN-Standard.txt</a>) */
	protected static final String SEVEN_TAG_ROSTER_KEYS = EVENT_TAG+" "+SITE_TAG+" "+DATE_TAG+" "+ROUND_TAG+" "+WHITE_TAG+" "+BLACK_TAG+" "+RESULT_TAG;
	
	/** The value of the PGN result tag when white won */
	protected static final String WHITE_WON = "1-0";
	/** The value of the PGN result tag when black won */
	protected static final String BLACK_WON = "0-1";
	/** The value of the PGN result tag when the game ended in a draw */
	protected static final String DRAW = "1/2-1/2";
	/** The value of the PGN result tag when the game is still playing */
	protected static final String PLAYING = "*";
	
		private static final int MAX_LINE_LENGTH = 80;
	
		/** Gets the PGN builder to test.
		 * @return a PGN builder
		*/
		@SuppressWarnings("unchecked")
		protected PGNConverter<B> getPGNBuilder() {
			return (PGNConverter<B>)adapter;
		}
	
		@Test
		@IfVariantSupported(Variant.CHESS960)
		@Tag("Chess960")
		@Tag("PGNTest.chess960")
		void chess960() {
			// Test with draw and non standard start FEN
			final String fen = "2r1k3/pp2pppp/2q5/8/1P6/8/4P3/3KR3 b q - 0 1"; //Change to black winning position
			final B board = adapter.fenToBoard(fen, Variant.CHESS960);
			assertTrue(board.makeMove(board.toMove("e8c8")));
			final String pgn = getPGNBuilder().toPGN(board);
			final Content parsed = parse(pgn);
			checkMadatoryTags(Variant.CHESS960, parsed, fen, BLACK_WON);
		}

		/** Gets the message to display when a tag value is wrong.
		 * @param tag the tag
		 * @return the message to display
		 */
		protected String wrongTag(String tag) {
			return tag+" tag value is wrong";
		}
		
		private void checkMadatoryTags(Variant variant, Content parsed, String expectedFEN, String expectedResult) {
			// Check that mandatory tags are there in the right order
			final String tagPairKeys = parsed.tagPairs().keySet().stream().collect(Collectors.joining(" "));
			assertTrue(tagPairKeys.startsWith(SEVEN_TAG_ROSTER_KEYS),String.format("PGN does not start with the seven tags roster (it starts with %s)", tagPairKeys));
			assertEquals(expectedResult, parsed.tagPairs().get(RESULT_TAG),wrongTag(RESULT_TAG));
	
			// Check variant tag
			String actualVariant = parsed.tagPairs.get(VARIANT_TAG);
			if (variant==Variant.CHESS960) {
				assertNotNull(actualVariant, "Missing "+VARIANT_TAG+" tag");
				assertEquals(actualVariant, actualVariant, wrongTag(VARIANT_TAG));
			} else {
				assertNull(actualVariant, "Should not have "+VARIANT_TAG+" tag");
			}
			
			// Check FEN tag
			final String actualFEN = parsed.tagPairs.get(FEN_TAG);
			if (expectedFEN!=null) {
				assertNotNull(actualFEN, "Missing "+FEN_TAG+" tag");
				assertFen(variant, expectedFEN, actualFEN);
			} else {
				assertNull(actualFEN, "Should not have "+FEN_TAG+" tag");
			}
		}
	
		@Test
		@Tag("PGNTest.basic")
		void basic() {
			// Lasker vs Thomas, 1912
			final B board = adapter.fenToBoard(STANDARD_START_FEN, Variant.STANDARD);
			addMoves(board, FATAL_ATTRACTION_MOVES.split(" "));
	
			final String pgn = getPGNBuilder().toPGN(board);
			
			final Content parsed = parse(pgn);
			checkMadatoryTags(Variant.STANDARD, parsed, null, WHITE_WON);
			assertEquals(7, parsed.tagPairs().size(),"Unexpected extra tags "+parsed.tagPairs());
			
			assertEquals("d4 e6 Nf3 f5 Nc3 Nf6 Bg5 Be7 Bxf6 Bxf6 e4 fxe4 Nxe4 b6 Ne5 O-O Bd3 Bb7 Qh5 Qe7 Qxh7+ Kxh7 Nxf6+ Kh6 Neg4+ Kg5 h4+ Kf4 g3+ Kf3 Be2+ Kg2 Rh2+ Kg1 Kd2#",
					parsed.moves().stream().collect(Collectors.joining(" ")));
		}
		
		@Test
		@Tag("PGNTest.nonStandardStart")
		void nonStandardStart() {
			// Test with draw and non standard start FEN
			final String fen = "r2qkbnr/ppp2ppp/2npb3/4p3/4P3/2NP1N2/PPP2PPP/R1BQKB1R w KQkq - 0 1";
			final B board = adapter.fenToBoard(fen, Variant.STANDARD);
			board.makeMove(board.toMove("f1e2"));
			final String pgn = getPGNBuilder().toPGN(board);
			final Content parsed = parse(pgn);
			checkMadatoryTags(Variant.STANDARD, parsed, fen, PLAYING);
		}
		
		@Test
		@Tag("PGNTest.draw")
		void draw() {
			// Test with draw and non standard start FEN
			final String fen = "1k6/8/K1Q5/8/8/8/8/8 b - - 0 1";
			final B board = adapter.fenToBoard(fen, Variant.STANDARD);
			final String pgn = getPGNBuilder().toPGN(board);
			final Content parsed = parse(pgn);
			checkMadatoryTags(Variant.STANDARD, parsed, fen, DRAW);
		}
	
		private void addMoves(B board, String[] uciMoves) {
			for (String mv:uciMoves) {
				board.makeMove(board.toMove(mv));
			}
		}
	
		/** The content of a PGN 
		 * @param tagPairs the tag pairs of the parsed PGN.
		 * @param moves the move list of the PGN
		*/
		protected record Content(Map<String, String> tagPairs, List<String> moves) {}
	 
		/** Parses a PGN string into a Content record.
		 * @param pgn the PGN string to parse
		 * @return a Content record with the parsed data
		 * @throws IllegalArgumentException if the PGN string is not valid
		 */
		protected Content parse(String pgn) {
			final String[] lines = pgn.split("\n");
			final Map<String, String> tagPairs = new LinkedHashMap<>();
			final List<String> moves = new ArrayList<>();
			boolean emptySeparatorLineFound = false;
			for (String line : lines) {
				// Check that no lines are bigger that 80 chars
				assertLineLength(line);
				if (line.startsWith("[") && line.endsWith("]")) {
					if (emptySeparatorLineFound) {
						throw new IllegalArgumentException("There is a 'tag pairs - move list' separator empty line in the tag pairs section");
					}
					parseTagPair(tagPairs, line);
					emptySeparatorLineFound = false;
				} else if (!line.isEmpty()) {
					// A move line
					if (!emptySeparatorLineFound) {
						throw new IllegalArgumentException("'tag pairs - move list' separator empty line is missing");
					}
					parseMoves(moves, line.split(" "));
				} else {
					if (emptySeparatorLineFound) {
						throw new IllegalArgumentException("There are more than one 'tag pairs - move list' separator empty lines");
					}
					emptySeparatorLineFound = true;
				}
			}
			return new Content(tagPairs, moves);
		}
	
		/** Parses a partial moves list and add them to a given list.
		 * @param moves the list to fill
		 * @param tokens the tokens to parse
		 */
		private void parseMoves(List<String> moves, String[] tokens) {
			for (String token : tokens) {
				if (!token.isEmpty() && !token.endsWith(".")) {
					moves.add(token);
				}
			}
		}

		private void parseTagPair(Map<String, String> tagPairs, String line) {
			line = line.substring(1, line.length() - 1);
			final int index = line.indexOf(' ');
			if (index == -1 || index == line.length() - 1) {
				// There's only one word in the tag
				throw new IllegalArgumentException("Invalid tag: " + line);
			}
			final String name = line.substring(0, index);
			final String value = line.substring(index + 1);
			if (name.isBlank() || value.isBlank() || value.charAt(0) != '"' || value.charAt(value.length() - 1) != '"') {
				throw new IllegalArgumentException("Invalid tag: " + line);
			}
			tagPairs.put(name, value.substring(1, value.length() - 1));
		}
	
		/** Asserts that the FEN tag value is correct
		 * <br>The default implementation uses {@link org.junit.jupiter.api.Assertions#assertEquals(String, String, String)} to check the FEN values.
		 * <br>Nevertheless, if you need a lenient check (for instance to tolerate deviations on move count or how castling rights are expressed),
		 * you can override this method.
		 * @param variant the chess game variant
		 * @param expectedFEN the expected FEN value
		 * @param actualFEN the actual FEN value
		 */
		protected void assertFen(Variant variant, String expectedFEN, String actualFEN) {
			assertEquals(expectedFEN, actualFEN, wrongTag(FEN_TAG));
		}
	
		/** Asserts that the line length does not exceed the recommended maximum 80 chars.
		 * <br>The default implementation uses {@link org.junit.jupiter.api.Assertions#assertTrue(boolean, String)} to check the line length.
		 * <br>Nevertheless, if you prefer not having this check, feel free to override this method.
		 * @param line the line to check
		 */
		protected void assertLineLength(String line) {
			assertTrue(line.length() <= MAX_LINE_LENGTH, "Line too long: " + line+" ("+line.length()+">"+MAX_LINE_LENGTH+")");
	}
}