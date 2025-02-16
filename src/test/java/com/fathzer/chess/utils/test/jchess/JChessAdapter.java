package com.fathzer.chess.utils.test.jchess;

import java.util.List;
import java.util.stream.Collectors;

import com.fathzer.chess.utils.model.TestAdapter;
import com.fathzer.chess.utils.model.Variant;
import com.fathzer.chess.utils.test.PGNTest.PGNConverter;
import com.fathzer.chess.utils.test.SANTest.SANConverter;
import com.fathzer.chess.utils.test.helper.Supports;
import com.fathzer.games.GameHistory;
import com.fathzer.jchess.Board;
import com.fathzer.jchess.CoordinatesSystem;
import com.fathzer.jchess.Move;
import com.fathzer.jchess.Piece;
import com.fathzer.jchess.fen.FENParser;
import com.fathzer.jchess.generic.BasicMove;
import com.fathzer.jchess.pgn.MoveAlgebraicNotationBuilder;
import com.fathzer.jchess.pgn.PGNHeaders;
import com.fathzer.jchess.pgn.PGNWriter;

@Supports(Variant.CHESS960)
public class JChessAdapter implements TestAdapter<JChessBoard, JChessMove>, SANConverter<JChessBoard, JChessMove>, PGNConverter<JChessBoard> {
	private static final MoveAlgebraicNotationBuilder sanConverter;
	
	static {
		sanConverter = new MoveAlgebraicNotationBuilder();
		sanConverter.withEnPassantSymbol("");
	}

	@Override
	public JChessBoard fenToBoard(String fen, Variant variant) {
		com.fathzer.jchess.Variant v = variant==Variant.CHESS960 ? com.fathzer.jchess.Variant.CHESS960 : com.fathzer.jchess.Variant.STANDARD;
		return new JChessBoard(new FENParser(fen, v).get());
	}

	@Override
	public JChessMove move(JChessBoard board, String uciMove) {
		final CoordinatesSystem cs = board.getBoard().getCoordinatesSystem();
		final int from = cs.getIndex(uciMove.substring(0, 2));
		final int to = cs.getIndex(uciMove.substring(2, 4));
		final String promotion = uciMove.length()>4 ? uciMove.substring(4, 5) : null;
		final Piece piece;
		if (promotion!=null) {
			// Warning the promotion code is always in lowercase in UCI
			final String notation = board.getBoard().isWhiteToMove() ? promotion.toUpperCase() : promotion;
			piece = Piece.ALL.stream().filter(p -> notation.equals(p.getNotation())).findAny().orElse(null);
		} else {
			piece = null;
		}
		return new JChessMove(new BasicMove(from, to, piece), board.getBoard());
	}

	@Override
	public String toPGN(JChessBoard board) {
		final GameHistory<Move, Board<Move>> history = board.toHistory();
		final List<String> lines = new PGNWriter().getPGN(new PGNHeaders.Builder().build(), history);
		return lines.stream().collect(Collectors.joining("\n"));
	}

	@Override
	public String getSAN(JChessMove move, JChessBoard board) {
		return sanConverter.get(board.getBoard(), move.mv);
	}
}
