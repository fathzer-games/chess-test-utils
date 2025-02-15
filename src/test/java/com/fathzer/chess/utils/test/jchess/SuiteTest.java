package com.fathzer.chess.utils.test.jchess;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import com.fathzer.chess.utils.test.Chess960Test;
import com.fathzer.chess.utils.test.PGNTest;
import com.fathzer.chess.utils.test.SANTest;

@Suite
@SuiteDisplayName("JChess-core Test Suite")
@SelectClasses({Chess960Test.class, SANTest.class, PGNTest.class})
//Prevent Sonar to complain about empty test class
@SuppressWarnings("java:S2187")
public class SuiteTest {
}