package com.fathzer.chess.utils.test.helper.fen;

/**
 * A class that can compare two FEN strings and relax some constraints.
 * <br>By default, FEN equality is checked strictly, but the following settings can change this behavior:
 * <br>- {@link #withStrictCastling(boolean)}: set to false to accept different equivalent castling rights representation
 * (for instance 'nbbqrknr/pppppppp/8/8/8/8/PPPPPPPP/NBBQRKNR w KQkq - 0 1' and 'nbbqrknr/pppppppp/8/8/8/8/PPPPPPPP/NBBQRKNR w HEhe - 0 1' are equivalent).
 * <br>- {@link #withStrictMoveNumber(boolean)}: set to false to accept different move numbers
 * (for instance 'nbbqrknr/pppppppp/8/8/8/8/PPPPPPPP/NBBQRKNR w KQkq - 0 1' and 'nbbqrknr/pppppppp/8/8/8/8/PPPPPPPP/NBBQRKNR w KQkq - 0 2' are equivalent).
 * @see #areEqual(String, String)
 */
public class FENComparator {
    private boolean strictCastling = true;
    private boolean strictMoveNumber = true;

    /**
     * Check if two FEN strings are equal.
     * @param fen1 the first FEN string
     * @param fen2 the second FEN string
     * @return true if the two FEN strings are equal, false otherwise
     * @throws IllegalArgumentException if the FEN strings are not valid
     * @see #withStrictCastling(boolean)
     * @see #withStrictMoveNumber(boolean)
     */
    public boolean areEqual(String fen1, String fen2) {
        if (strictCastling) {
            return fen1.equals(fen2);
        } else {
            // Check excluding castling rights
            String[] fen1Parts = fen1.split(" ");
            String[] fen2Parts = fen2.split(" ");
            if (fen1Parts.length != 6 || fen2Parts.length != 6) {
                throw new IllegalArgumentException("Invalid FEN, FEN should have 6 parts");
            }
            if (!fen1Parts[0].equals(fen2Parts[0]) || !fen1Parts[1].equals(fen2Parts[1]) || !fen1Parts[3].equals(fen2Parts[3]) || !fen1Parts[4].equals(fen2Parts[4]) || (strictMoveNumber && !fen1Parts[5].equals(fen2Parts[5]))) {
                return false;
            }
            return areCastlingRightsEquivalent(fen1Parts[2], fen2Parts[2], fen1Parts[0]);
        }
    }
            
    /**
     * Check if two castling rights strings are equivalent.
     * @param rights1 the first castling rights string
     * @param rights2 the second castling rights string
     * @param pieces the pieces on the board expressed in FEN format
     * @return true if the two castling rights strings are equivalent, false otherwise
     */
    public static boolean areCastlingRightsEquivalent(String rights1, String rights2, String pieces) {
        if (rights1.equals(rights2)) {
            return true;
        }
        if (rights1.length() != rights2.length()) {
            return false;
        }
        final String[] pieceLines = pieces.split("/");
        for (int i = 0; i < rights1.length(); i++) {
            if (!areSameCastlingRights(rights1.charAt(i), rights2.charAt(i), pieceLines)) {
                return false;
            }
        }
        return true;
    }

    private static boolean areSameCastlingRights(char right1, char right2, String[] pieces) {
        if (right1 == right2) {
            return true;
        } else if (Character.isUpperCase(right1) != Character.isUpperCase(right2)) {
            return false;
        }
        final boolean white = Character.isUpperCase(right1);
        final String interestingPieces = pieces[white ? pieces.length - 1 : 0];
        if (white) {
            right1 = Character.toLowerCase(right1);
            right2 = Character.toLowerCase(right2);
        }
        if (right1 == 'k' || right2 == 'k') {
            return checkOuterRook(interestingPieces, right1 == 'k' ? right2 : right1, true);
        } else if (right1 == 'q' || right2 == 'q') {
            return checkOuterRook(interestingPieces, right1 == 'q' ? right2 : right1, false);
        }
        return false;
    }
                        
    private static boolean checkOuterRook(String interestingPieces, char right, boolean kingSide) {
        final int fileIndex = getOuterFile(interestingPieces, kingSide);
        return fileIndex >= 0 && right == ('a' + fileIndex);
    }

    static int getOuterFile(String interestingPieces, boolean kingSide) {
        int fileIndex = kingSide ? getFileCount(interestingPieces)-1 : 0;
        if (kingSide) {
            for (int i = interestingPieces.length()-1; i >= 0; i--) {
                final char c = interestingPieces.charAt(i);
                final char lowerCased = Character.toLowerCase(c);
                if (lowerCased == 'r') {
                    break;
                } else if (lowerCased == 'k') {
                    fileIndex = -1;
                    break;
                } else if (Character.isDigit(c)) {
                    fileIndex = fileIndex - Character.getNumericValue(c);
                } else {
                    fileIndex--;
                }
            }
        } else {
            for (int i = 0; i < interestingPieces.length(); i++) {
                final char c = interestingPieces.charAt(i);
                final char lowerCased = Character.toLowerCase(c);
                if (lowerCased == 'r') {
                    break;
                } else if (lowerCased == 'k') {
                    fileIndex = -1;
                    break;
                } else if (Character.isDigit(c)) {
                    fileIndex = fileIndex + Character.getNumericValue(c);
                } else {
                    fileIndex++;
                }
           }
        }
        return fileIndex;
    }

    private static int getFileCount(String interestingPieces) {
        int result = interestingPieces.length();
        for (int i = 0; i < interestingPieces.length(); i++) {
            if (Character.isDigit(interestingPieces.charAt(i))) {
                result = result + Character.getNumericValue(interestingPieces.charAt(i)) - 1;
            }
        }
        return result;
    }
            
    /**
     * Set the strict castling rights mode.
     * @param strict if true, the {@link #areEqual(String, String)} will check for strict castling rights equality, otherwise it will accept different equivalent castling rights representation
     * (for instance 'nbbqrknr/pppppppp/8/8/8/8/PPPPPPPP/NBBQRKNR w KQkq - 0 1' and 'nbbqrknr/pppppppp/8/8/8/8/PPPPPPPP/NBBQRKNR w HEhe - 0 1' are equivalent).
     * @return this
     */
    public FENComparator withStrictCastling(boolean strict) {
        this.strictCastling = strict;
        return this;
    }

    /**
     * Set the strict mode.
     * @param strict if true, the {@link #areEqual(String, String)} will check for strict FEN equality, otherwise it will accept different equivalent castling rights representation
     * (for instance 'nbbqrknr/pppppppp/8/8/8/8/PPPPPPPP/NBBQRKNR w KQkq - 0 1' and 'nbbqrknr/pppppppp/8/8/8/8/PPPPPPPP/NBBQRKNR w HEhe - 0 1' are equivalent).
     * @return this
     */
    public FENComparator withStrictMoveNumber(boolean strict) {
        this.strictMoveNumber = strict;
        return this;
    }

}


