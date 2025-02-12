package com.fathzer.chess.utils.test.jchess;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fathzer.chess.utils.test.AbstractPGNTest;
import com.fathzer.chess.utils.test.helper.ExcludeMethods;
import com.fathzer.games.GameHistory;
import com.fathzer.jchess.Board;
import com.fathzer.jchess.Move;
import com.fathzer.jchess.pgn.PGNHeaders;
import com.fathzer.jchess.pgn.PGNWriter;

@ExcludeMethods({"testDraw","testNonStandardStart"})
class PGNTest extends AbstractPGNTest<JChessBoard, Move>{

	@Override
	protected Function<JChessBoard, String> getPGNBuilder() {
		return b -> {
			final GameHistory<Move, Board<Move>> history = b.toHistory();
			final List<String> lines = new PGNWriter().getPGN(new PGNHeaders.Builder().build(), history);
			return lines.stream().collect(Collectors.joining("\n"));
		};
	}
}