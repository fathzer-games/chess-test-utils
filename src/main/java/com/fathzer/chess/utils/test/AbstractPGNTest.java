package com.fathzer.chess.utils.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fathzer.chess.utils.model.IBoard;
import com.fathzer.chess.utils.model.Variant;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public abstract class AbstractPGNTest<B extends IBoard<M>, M> extends AbstractAdaptableTest<B, M> {
	// Lasker vs Thomas, 1912
	protected static final String FATAL_ATTRACTION_MOVES = "d2d4 e7e6 g1f3 f7f5 b1c3 g8f6 c1g5 f8e7 g5f6 e7f6 e2e4"
			+ " f5e4 c3e4 b7b6 f3e5 e8g8 f1d3 c8b7 d1h5 d8e7 h5h7 g8h7 e4f6 h7h6 e5g4 h6g5 h2h4 g5f4 g2g3 f4f3 d3e2"
			+ " f3g2 h1h2 g2g1 e1d2";

    protected static final String EVENT_TAG = "Event";
    protected static final String SITE_TAG = "Site";
    protected static final String DATE_TAG = "Date";
    protected static final String ROUND_TAG = "Round";
    protected static final String FEN_TAG = "FEN";
    protected static final String VARIANT_TAG = "Variant";
    protected static final String RESULT_TAG = "Result";
    protected static final String WHITE_TAG = "White";
    protected static final String BLACK_TAG = "Black";
    
    // https://github.com/fsmosca/PGN-Standard/blob/master/PGN-Standard.txt 8.1.1
    protected static final String SEVEN_TAG_ROSTER_KEYS = EVENT_TAG+" "+SITE_TAG+" "+DATE_TAG+" "+ROUND_TAG+" "+WHITE_TAG+" "+BLACK_TAG+" "+RESULT_TAG;
    
    protected static final String UNKNOWN = "?";

    protected static final String WHITE_WON = "1-0";
    protected static final String BLACK_WON = "0-1";
    protected static final String DRAW = "1/2-1/2";
    protected static final String PLAYING = "*";

    protected abstract Function<B, String> getPGNBuilder();

    @Disabled
    @Test
    void test960() {
        //TODO
        fail("Not yet implemented");
    }

    @Test
    void testPGN() {
    	Function<B, String> pgnBuilder = getPGNBuilder();
    	
        // Lasker vs Thomas, 1912
        B board = u.fenToBoard(STANDARD_START_FEN, Variant.STANDARD);
        addMoves(board, FATAL_ATTRACTION_MOVES.split(" "));

        String pgn = pgnBuilder.apply(board);
        
        // Check that no lines are bigger that 80 chars
        String[] lines = pgn.split("\n");
        for (String line : lines) {
            assertTrue(line.length() <= 80, "Line too long: " + line+" ("+line.length()+">80)");
        }
        
        Content parsed = parse(pgn);
        assertFalse(parsed.tagPairs.containsKey(VARIANT_TAG), "Variant tag found when not expected");
        assertFalse(parsed.tagPairs.containsKey(FEN_TAG), "FEN tag found when not expected");
        // Check that mandatory tags are there in the right order
        String expectedOrderedTags = EVENT_TAG+" "+SITE_TAG+" "+DATE_TAG+" "+ROUND_TAG+" "+WHITE_TAG+" "+BLACK_TAG+" "+RESULT_TAG;
        assertEquals(expectedOrderedTags, parsed.tagPairs.keySet().stream().collect(Collectors.joining(" ")));
        assertEquals(WHITE_WON, parsed.tagPairs.get(RESULT_TAG), "Wrong result tag");
        assertEquals("d4 e6 Nf3 f5 Nc3 Nf6 Bg5 Be7 Bxf6 Bxf6 e4 fxe4 Nxe4 b6 Ne5 O-O Bd3 Bb7 Qh5 Qe7 Qxh7+ Kxh7 Nxf6+ Kh6 Neg4+ Kg5 h4+ Kf4 g3+ Kf3 Be2+ Kg2 Rh2+ Kg1 Kd2#",
                parsed.moves().stream().collect(Collectors.joining(" ")));
    }
    
    @Test
    void testDrawNonStandardStart() {
        // Test with draw and non standard start FEN
    	//TODO
    }

    private void addMoves(B board, String[] uciMoves) {
        for (String mv:uciMoves) {
            board.makeMove(u.legalMove(board, mv));
        }
    }

    protected record Content(Map<String, String> tagPairs, List<String> moves) {}
 
    protected Content parse(String pgn) {
        final String[] lines = pgn.split("\n");
        final Map<String, String> tagPairs = new LinkedHashMap<>();
        final List<String> moves = new ArrayList<>();
        boolean emptySeparatorLine = false;
        for (String line : lines) {
            if (line.startsWith("[") && line.endsWith("]")) {
            	if (emptySeparatorLine) {
            		throw new IllegalArgumentException("There is a 'tag pairs - move list' separator empty line in the tag pairs section");
            	}
                line = line.substring(1, line.length() - 1);
                final int index = line.indexOf(' ');
                if (index == -1 || index == line.length() - 1) {
                    throw new IllegalArgumentException("Invalid tag: " + line);
                }
                final String name = line.substring(0, index);
                final String value = line.substring(index + 1);
                if (name.isBlank() || value.isBlank() || value.charAt(0) != '"' || value.charAt(value.length() - 1) != '"') {
                    throw new IllegalArgumentException("Invalid tag: " + line);
                }
                tagPairs.put(name, value.substring(1, value.length() - 1));
                emptySeparatorLine = false;
            } else if (!line.isEmpty()) {
                // A move line
            	if (!emptySeparatorLine) {
            		throw new IllegalArgumentException("'tag pairs - move list' separator empty line is missing");
            	}
                parseMoves(moves, line.split(" "));
            } else {
            	if (emptySeparatorLine) {
            		throw new IllegalArgumentException("There are more than one 'tag pairs - move list' separator empty lines");
            	}
            	emptySeparatorLine = true;
            }
        }
        return new Content(tagPairs, moves);
    }

    protected static void parseMoves(List<String> moves, String[] tokens) {
        for (String token : tokens) {
            if (!token.isEmpty() && !token.endsWith(".")) {
                moves.add(token);
            }
        }
    }
}