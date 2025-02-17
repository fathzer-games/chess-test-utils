package com.fathzer.chess.utils.test.helper.perft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.fathzer.chess.utils.model.IBoard;

public class PerfT<M> {
	
	public record Result<T>(Collection<Divide<T>> divides) {
		public long getNbLeaves() {
			return divides.stream().mapToLong(Divide::getCount).sum();
		}
	}

	public Result<M> divide(final IBoard<M> generator, final int depth) {
		if (depth <= 0) {
            throw new IllegalArgumentException("Search depth MUST be > 0");
		}
		final List<M> moves = generator.getMoves();
		final List<Divide<M>> divides = new ArrayList<>(moves.size());
		for (M move : moves) {
			final Optional<Divide<M>> divide = getPrfT(generator, move, depth - 1);
			divide.ifPresent(divides::add);
		}
		return new Result<>(divides);
	}

	private Optional<Divide<M>> getPrfT(IBoard<M> moveGenerator, M move, int depth) {
		final long leaves;
		if (depth==0 && moveGenerator.isGetMovesLegal()) {
			leaves = 1;
		} else {
			if (moveGenerator.makeMove(move)) {
				leaves = get(moveGenerator, depth);
				moveGenerator.unmakeMove();
			} else {
				leaves = 0;
			}
		}
		return Optional.ofNullable(leaves == 0 ? null : new Divide<>(move, leaves));
	}
	
    private long get (IBoard<M> moveGenerator, final int depth) {
    	if (depth==0) {
    		return 1;
    	}
		final List<M> moves = moveGenerator.getMoves();
		if (depth==1 && moveGenerator.isGetMovesLegal()) {
			return moves.size();
		}
		long count = 0;
		for (M move : moves) {
            if (moveGenerator.makeMove(move)) {
	            count += get(moveGenerator, depth-1);
	            moveGenerator.unmakeMove();
            }
		}
        return count;
    }
}