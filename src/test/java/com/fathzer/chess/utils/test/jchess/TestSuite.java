package com.fathzer.chess.utils.test.jchess;

import org.junit.platform.suite.api.AfterSuite;
import org.junit.platform.suite.api.BeforeSuite;
import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import com.fathzer.chess.utils.test.Chess960Test;
import com.fathzer.chess.utils.test.PGNTest;
import com.fathzer.chess.utils.test.SANTest;

@Suite
//@SuiteDisplayName("Parameterized Test Suite")
@SelectClasses({Chess960Test.class, SANTest.class, PGNTest.class})
@ExcludeTags("Chess960Test.fuckingTest")
public class TestSuite {
	@BeforeSuite
	static void before() {
		System.out.println("Before suite");
	}

	@AfterSuite
	static void after() {
		System.out.println("After suite");
	}
}