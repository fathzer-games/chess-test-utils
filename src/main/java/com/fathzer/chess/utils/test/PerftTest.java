package com.fathzer.chess.utils.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import com.fathzer.chess.utils.model.IBoard;
import com.fathzer.chess.utils.model.Variant;
import com.fathzer.chess.utils.test.helper.perft.PerfT;
import com.fathzer.chess.utils.test.helper.perft.PerfTResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

/** 
 * A test class based on [Perft](https://www.chessprogramming.org/Perft).
 * <br>This class uses the data set available in [jchess-perft-dataset](https://github.com/fathzer-games/jchess-perft-dataset).
 */
public class PerftTest<B extends IBoard<M>, M> extends AbstractAdaptableTest<B, M> {
    @Test
    @Tag("PerftTest.standardSuite")
    @DisabledIfSystemProperty(named="perftDepth", matches = "0")
    void standardSuite() throws IOException {
       	doTestSuite("perftDepth", 2, readTests("/com/fathzer/jchess/perft/Perft.epd"), Variant.STANDARD);
    }

	@Test
    @Tag("PerftTest.chess960Suite")
	@IfVariantSupported(Variant.CHESS960)
    @DisabledIfSystemProperty(named="perftChess960Depth", matches = "0")
    void chess960Suite() throws IOException {
		doTestSuite("perftChess960Depth", 2, readTests("/com/fathzer/jchess/perft/Perft960.epd"), Variant.CHESS960);
    }
	
	private List<String> readTests(String resource) throws IOException {
    	try (BufferedReader stream = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(resource), StandardCharsets.UTF_8))) {
        	return stream.lines().toList();
    	}
	}

    private void doTestSuite(String depthProperty, int defaultDepth, List<String> tests, Variant variant) {
		final int depth = Integer.getInteger(depthProperty, defaultDepth);
        if (depth!=defaultDepth) {
        	System.err.println(depthProperty+": "+depth+", "+tests.size()+" lines");
        }
        final PerfT<M> perfT = new PerfT<>();
        tests.stream().parallel().forEach(line -> {
            String[] parts = line.split(";");
            String fen = parts[0];
            if (parts.length<=depth) {
            	return;
            }
            long expectedTotalMoves = Long.parseLong(parts[depth].split(" ")[1].trim());
            try {
	            final IBoard<M> board = u.fenToBoard(fen, variant);
	            final PerfTResult<M> result = perfT.divide(board, depth);
	            assertEquals(expectedTotalMoves, result.getNbLeaves(), String.format("Fen: %s, Depth: %s, Expected: %s, Actual: %s", fen, depth, expectedTotalMoves, result.getNbLeaves()));
			} catch (NullPointerException e) {
				throw new RuntimeException("Fen: " + fen, e);
			}
        });
	}
}
