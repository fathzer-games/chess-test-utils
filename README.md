# chess-test-utils
A test framework for chess libraries

Testing is a boring complicated nightmare...
That's probably the reason why most of the chess libraries I've tested, including mine, had (minor) bugs.
The purpose of this library is to make it easy to write exhaustive tests for chess libraries.

It contains a set of abstract [JUnit5](https://junit.org/junit5) test classes to test various functionalities of chess libraries ([move generator](https://www.chessprogramming.org/Move_Generation), [FEN parser](https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation), [Chess960](https://en.wikipedia.org/wiki/Chess960) compliance, etc...).

## TOC

- [chess-test-utils](#chess-test-utils)
	- [TOC](#toc)
	- [How to install](#how-to-install)
	- [Write your first test](#write-your-first-test)
	- [Available tests](#available-tests)
		- [PerfTTest](#perfttest)
		- [Chess960Test](#chess960test)
		- [SANTest](#santest)
		- [PGNTest](#pgntest)
	- [Advanced usage - Filtering tests](#advanced-usage---filtering-tests)
		- [Exclude some methods from test classes](#exclude-some-methods-from-test-classes)
		- [Extend classes](#extend-classes)

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

Then write concrete implementations of `Iboard` and `TestAdapter`
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
If your chess library supports [Chess960](https://en.wikipedia.org/wiki/Chess960), add the `@Supports(Variant.Chess960)` to the class.

```java
    @Supports(Variant.Chess960)
    public class MyLibAdapter implements TestAdapter<MyBoard, Move> {
      ...
    }
```

Last thing: The adapter is discovered using the [java service loader](https://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html) mechanism.
You have to create a resource file named `com.fathzer.chess.utils.model.TestAdapter` file in the `/src/test/resources/META-INF/services/` directory of your project with the following content (assuming your adapter class name is `com.mylib.ChessLibTestAdapter`):
`com.mylib.ChessLibTestAdapter`

Now, your're ready for testing!

## Write your first test

The first test that any chess engine author should try is
[PerfTTest](#PerfTTest) (see below for a description of this test).

To do this, simply create a JUnit5 test suite in the `src/test/java` directory of your project and add the following code in it:
```java
@Suite
@SuiteDisplayName("Tests from chess-test-utils")
@SelectClasses({PerftTest.class})
//Prevent Sonar to complain about empty test class
@SuppressWarnings("java:S2187")
public class SuiteTest {
}
```

Thats all, this suite will be run with your other tests by `mvn test` command.
By default, the PerfTTest will be run at depth 2 (see the [PerftTest documentation](#PerftTest) to known how to configure it).

You can add more tests to this suite (see below for the [list of available tests](#available-tests)).

## Available tests
All tests are in `com.fathzer.chess.utils.test` package.
Some requires extra interfaces to be implemented by your chess library adapter (see test documentation for more information).

### PerfTTest
This test implements the [PerfT test](https://www.chessprogramming.org/Perft) performance test. It uses the test set provided by the [jchess-perft-dataset](https://github.com/fathzer-games/jchess-perft-dataset) project containing 6969 standard chess positions and 960 chess960 positions.
By default, the depth of both standard and chess960 tests is 2. You can change it by setting the `perftDepth` or `chess960PerftDepth` system properties.
For example, to run the standard test at depth 4 and chess960test at depth 2, you can use `mvn test -DperftDepth=4`.

### Chess960Test
This test implements some specific tests for [Chess960](https://www.chessprogramming.org/Chess960) move generators.
It requires your adapter to have the `@Supports(Variant.Chess960)` annotation and to implement the `com.fathzer.chess.utils.model.BoardPieceScanner` interface.

### SANTest
This test implements some tests for [SAN](https://en.wikipedia.org/wiki/Algebraic_notation_(chess)) converters.
It requires your adapter to implement the `com.fathzer.chess.utils.test.SANTest.SANConverter` interface.

### PGNTest
This test implements some tests for [PGN](https://www.chessprogramming.org/PGN) builders.
It requires your adapter to implement the `com.fathzer.chess.utils.test.PGNTest.PGNParser` interface.

## Advanced usage - Filtering tests

### Exclude some methods from test classes
// TODO
@ExcludeTags

### Extend classes
