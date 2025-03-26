package com.fathzer.chess.utils.test.helper.fen;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FENComparatorTest {
    @Test
    void test() {
        assertEquals(7, FENComparator.getOuterFile("rnbqkbnr", true));
        assertEquals(0, FENComparator.getOuterFile("rnbqkbnr", false));
        assertEquals(-1, FENComparator.getOuterFile("rnbqkbn1", true));
        assertEquals(-1, FENComparator.getOuterFile("1nbqkbnr", false));
        assertEquals(-1, FENComparator.getOuterFile("nbqq2bn", false));

        assertTrue(FENComparator.areCastlingRightsEquivalent("Gkq", "Gga", "rn2k1r1/ppp1pp1p/3p2p1/5bn1/P7/2N2B2/1PPPPP2/2BNK1RR"));
    }

    @Test
    void testAreEqual() {
        FENComparator comparator = new FENComparator();

        assertThrows(IllegalArgumentException.class, () -> comparator.areEqual("rn2k1r1/ppp1pp1p/3p2p1/5bn1/P7/2N2B2/1PPPPP2/2BNK1RR w - 0", "rn2k1r1/ppp1pp1p/3p2p1/5bn1/P7/2N2B2/1PPPPP2/2BNK1RR w - 0 1"));

        String fen1 = "nbbqrknr/pppppppp/8/8/8/8/PPPPPPPP/NBBQRKNR w KQkq - 0 1";
        String fen2 = "nbbqrknr/pppppppp/8/8/8/8/PPPPPPPP/NBBQRKNR w HEhe - 0 1";
        assertFalse(comparator.areEqual(fen1, fen2));
        comparator.withStrictCastling(false);
        assertTrue(comparator.areEqual(fen1, fen2));
        assertFalse(comparator.areEqual(fen1, "nbbqrknr/pppppppp/8/8/8/8/PPPPPPPP/NBBQRKNR w HEh - 0 1"));
        assertFalse(comparator.areEqual(fen1, "nbbqrknr/pppppppp/8/8/8/8/PPPPPPPP/NBBQRKNR w HEhd - 0 1"));
        assertFalse(comparator.areEqual(fen1, "nbbqrknr/pppppppp/8/8/8/8/PPPPPPPP/NBBQRKNR w HEhE - 0 1"));
        assertFalse(comparator.areEqual(fen2, "nbbqrknr/pppppppp/8/8/8/8/PPPPPPPP/NBBQRKNR w HEhd - 0 1"));

        String fen3 = "nbbqrknr/pppppppp/8/8/8/8/PPPPPPPP/NBBQRKNR w KQkq - 0 4";
        assertFalse(comparator.areEqual(fen1, fen3));
        comparator.withStrictMoveNumber(false);
        assertTrue(comparator.areEqual(fen1, fen3));
    }
}
