package com.fathzer.chess.utils.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class AdapterLoaderTest {
    private static final String CHESS_TEST_UTILS_ADAPTER = "chess-test-utils.adapter";
    private static String property;

    @BeforeAll
    static void setUp() {
        property = System.getProperty(CHESS_TEST_UTILS_ADAPTER);
    }

    @AfterAll
    static void tearDown() {
        if (property == null) {
            System.clearProperty(CHESS_TEST_UTILS_ADAPTER);
        } else {
            System.setProperty(CHESS_TEST_UTILS_ADAPTER, property);
        }
    }

    @Test
    void test() {
        System.setProperty(CHESS_TEST_UTILS_ADAPTER, "com.fathzer.chess.utils.test.UnknownClass");
        final AdapterLoader adapterLoaderKo = new AdapterLoader();
        assertThrows(IllegalStateException.class, adapterLoaderKo::get);

        System.setProperty(CHESS_TEST_UTILS_ADAPTER, com.fathzer.chess.utils.test.jchess.JChessAdapter.class.getName());
        final AdapterLoader adapterLoaderOk = new AdapterLoader();
        assertNotNull(adapterLoaderOk.get());
    }

}
