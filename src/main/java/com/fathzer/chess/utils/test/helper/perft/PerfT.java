package com.fathzer.chess.utils.test.helper.perft;

import java.util.List;
import java.util.Optional;

import com.fathzer.chess.utils.model.IBoard;

public class PerfT<M> {

	public PerfTResult<M> divide(final IBoard<M> generator, final int depth) {
		if (depth <= 0) {
            throw new IllegalArgumentException("Search depth MUST be > 0");
		}
		final List<M> moves = generator.getMoves();
		final PerfTResult<M> result = new PerfTResult<>();
		result.addMovesFound(moves.size());
		for (M move : moves) {
			final Optional<Divide<M>> divide = getPrfT(generator, move, depth - 1, result);
			divide.ifPresent(result::add);
		}
		return result;
	}

	private Optional<Divide<M>> getPrfT(IBoard<M> moveGenerator, M move, int depth, PerfTResult<M> result) {
		final long leaves;
		if (depth==0 && moveGenerator.isGetMovesLegal()) {
			leaves = 1;
		} else {
			if (moveGenerator.makeMove(move)) {
				result.addMoveMade();
				leaves = get(moveGenerator, depth, result);
				moveGenerator.unmakeMove();
			} else {
				leaves = 0;
			}
		}
		return Optional.ofNullable(leaves == 0 ? null : new Divide<>(move, leaves));
	}
	
    private long get (IBoard<M> moveGenerator, final int depth, PerfTResult<M> result) {
    	if (depth==0) {
    		return 1;
    	}
		final List<M> moves = moveGenerator.getMoves();
		result.addMovesFound(moves.size());
		if (depth==1 && moveGenerator.isGetMovesLegal()) {
			return moves.size();
		}
		long count = 0;
		for (M move : moves) {
            if (moveGenerator.makeMove(move)) {
	            result.addMoveMade();
	            count += get(moveGenerator, depth-1, result);
	            moveGenerator.unmakeMove();
            }
		}
        return count;
    }
}