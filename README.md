# chess-test-utils
A test framework for chess libraries

Testing is a boring complicated nightmare...
That's probably the reason why most of the chess libraries I've tested had (minor) bugs.
The purpose of this library is to make it easy to write exhaustive tests for chess libraries.

It contains a set of abstract [JUnit5](https://junit.org/junit5) test classes to test various functionalities of chess libraries ([move generator](https://www.chessprogramming.org/Move_Generation), [FEN parser](https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation), [Chess960](https://en.wikipedia.org/wiki/Chess960) compliance, etc...).

## TOC

- [How to install](#How-to-install)
- [Write your first test](#Write-your-first-test)
- [Available tests](#Available-tests)
- [Advanced usage - Filtering tests](#Advanced-usage---Filtering-tests)

## How to install
To use it, start by adding the following dependency in your project:

```xml
<dependency>
    <groupId>com.fathzer</groupId>
    <artifactId>chess-test-utils</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

Then write a concrete implementation of `Iboard` and a subclass of `TestAdapter`
`IBoard` is the interface used by the chess libraries to represent a chess board and access to basic functionalities like playing moves or getting the list of possible moves.
Usually a board implementation is straight-forward because its methods are probably already implemented in the chess library you're testing. 

Here is an example based on [chesslib](https://github.com/bhlangonijr/chesslib):

```java
import java.util.List;

import com.fathzer.chess.utils.model.IBoard;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;

public class ChessLibBoard implements IBoard<Move>{
	private final Board board;

	public ChessLibBoard(Board board) {
		this.board = board;
	}
	
	@Override
	public List<Move> getMoves() {
		return board.pseudoLegalMoves();
	}

	@Override
	public boolean makeMove(Move mv) {
		return board.doMove(mv);
	}

	@Override
	public void unmakeMove() {
		board.undoMove();
	}

	public Board getBoard() {
		return board;
	}
}
```

The subclass of `TestAdapter` should also be very simple.
Here is an example also based on [chesslib](https://github.com/bhlangonijr/chesslib):
```java
class ChessLibTestAdapter extends TestAdapter<ChessLibBoard, Move> {
import com.fathzer.chess.utils.model.TestAdapter;
import com.fathzer.chess.utils.model.Variant;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;

public class ChessLibAdapter implements TestAdapter<ChessLibBoard, Move> {

	@Override
	public ChessLibBoard fenToBoard(String fen, Variant variant) {
		final Board internalBoard = new Board();
		internalBoard.loadFromFen(fen);
		return new ChessLibBoard(internalBoard);
	}

	@Override
	public Move move(ChessLibBoard board, String uciMove) {
		final String from = uciMove.substring(0, 2);
		final String to = uciMove.substring(2, 4);
		final String promotion = uciMove.length()>4 ? uciMove.substring(4, 5) : null;
		Piece p = null;
		if (promotion!=null) {
			// Warning the promotion code is always in lowercase in UCI
			final String notation = board.getBoard().getSideToMove()==Side.WHITE ? promotion.toUpperCase() : promotion;
			p = Piece.fromFenSymbol(notation);
		} else {
			p = Piece.NONE;
		}
		return new Move(Square.fromValue(from.toUpperCase()), Square.fromValue(to.toUpperCase()), p);
	}
}
```
If your chess library supports [Chess960](https://en.wikipedia.org/wiki/Chess960), please override the `isSupported(Variant)` as in this example.
```java
    @Override
    public boolean isSupported(Variant variant) {
        return true;
    }
```

Last thing: The adapter is automatically registered using the [java service loader](https://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html) mechanism.
You have to create a resource file named `com.fathzer.chess.utils.model.TestAdapter` file in the `/src/test/resources/META-INF/services/` directory of your project with the following content (assuming your adapter class name is `com.mylib.ChessLibTestAdapter`):
`com.mylib.ChessLibTestAdapter`


Now, your're ready for testing!

## Write your first test

Let suppose you have a class that converts a chess move to [Standard Algebraic Notation (SAN)](https://en.wikipedia.org/wiki/Algebraic_notation_(chess)) notation.

//To be completed

## Available tests

## Advanced usage - Filtering tests
