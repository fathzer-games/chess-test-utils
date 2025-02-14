package com.fathzer.chess.utils.test.jchess;

import org.junit.platform.suite.api.AfterSuite;
import org.junit.platform.suite.api.BeforeSuite;
import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import com.fathzer.chess.utils.test.Chess960Test;
import com.fathzer.chess.utils.test.PGNTest;
import com.fathzer.chess.utils.test.SANTest;

@Suite
@SuiteDisplayName("JChess-core Test Suite")
@SelectClasses({Chess960Test.class, SANTest.class, PGNTest.class})
//FIXME
@ExcludeTags({"Chess960Test.castlingOnStandardStartPosition",
	"SANTest.chess960Castling",
	"PGNTest.chess960", "PGNTest.draw", "PGNTest.nonStandardStart"})
public class SuiteTest {
	@BeforeSuite
	static void before() {
		System.out.println("Before suite");
	}

	@AfterSuite
	static void after() {
		System.out.println("After suite");
	}
}