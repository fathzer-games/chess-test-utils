package com.fathzer.chess.utils.test.helper.perft;

/** A <a href="https://www.chessprogramming.org/Perft#Divide">perft divide</a> result for a specific move.
 * @param <M> the type of the move
 * @param move the move
 * @param count the number of leaves for this move
 */
public record Divide<M>(M move, long count) {

	@Override
	public String toString() {
		return move.toString()+": "+count;
	}
}

