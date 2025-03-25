package com.fathzer.chess.utils.test.jchess;

import java.util.List;
import java.util.stream.Collectors;

import com.fathzer.chess.utils.model.BoardPieceScanner;
import com.fathzer.chess.utils.model.TestAdapter;
import com.fathzer.chess.utils.model.Variant;
import com.fathzer.chess.utils.test.PGNTest.PGNConverter;
import com.fathzer.chess.utils.test.SANTest.SANConverter;
import com.fathzer.chess.utils.test.helper.Supports;
import com.fathzer.games.Color;
import com.fathzer.games.GameHistory;
import com.fathzer.jchess.Board;
import com.fathzer.jchess.Move;
import com.fathzer.jchess.Piece;
import com.fathzer.jchess.fen.FENParser;
import com.fathzer.jchess.pgn.MoveAlgebraicNotationBuilder;
import com.fathzer.jchess.pgn.PGNHeaders;
import com.fathzer.jchess.pgn.PGNWriter;

@Supports(Variant.CHESS960)
public class JChessAdapter implements TestAdapter<JChessBoard, JChessMove>, SANConverter<JChessBoard, JChessMove>, PGNConverter<JChessBoard>, BoardPieceScanner<JChessBoard> {
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
	public String toPGN(JChessBoard board) {
		final GameHistory<Move, Board<Move>> history = board.toHistory();
		final List<String> lines = new PGNWriter().getPGN(new PGNHeaders.Builder().build(), history);
		return lines.stream().collect(Collectors.joining("\n"));
	}

	@Override
	public String getSAN(JChessMove move, JChessBoard board) {
		return sanConverter.get(board.getBoard(), move);
	}

	@Override
	public int getPiece(JChessBoard board, String algebraicNotation) {
		final Piece piece = board.getBoard().getPiece(board.getBoard().getCoordinatesSystem().getIndex(algebraicNotation));
		if (piece==null) {
			return 0;
		} else {
			int result = (piece.ordinal()+1)/2;
			return Color.WHITE==piece.getColor() ? result : -result; 
		}
	}
}
