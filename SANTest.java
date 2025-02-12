package com.fathzer.chess.utils.test.chesslib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.fathzer.chess.utils.test.AbstractSANTest;
import com.fathzer.chess.utils.test.helper.ExcludeTags;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveList;

@ExcludeTags("IllegalMove")
public class SANTest extends AbstractSANTest<ChessLibBoard, Move> {
	private static final Method ENCODER;
	
	static {
		Method encoder = null;
		try {
			encoder = MoveList.class.getDeclaredMethod("encodeToSan", Board.class, Move.class);
			encoder.setAccessible(true);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ENCODER = encoder;
	}

	@Override
	protected SANConverter<ChessLibBoard, Move> getSANConverter() {
		return (m,b) -> { 
			try {
				return (String) ENCODER.invoke(ENCODER, b.getBoard(), m);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		};
	}
}
