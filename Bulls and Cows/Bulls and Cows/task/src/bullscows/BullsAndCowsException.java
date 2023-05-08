package bullscows;

public abstract class BullsAndCowsException extends RuntimeException {
    public BullsAndCowsException(String message) {
        super(message);
    }

    public static class NotEnoughSymbols extends BullsAndCowsException {
        public NotEnoughSymbols(int codeLength, int symbols) {
            super("Error: it's not possible to generate a code with a length of " + codeLength + " with " + symbols + " unique symbols.");
        }
    }

    public static class InvalidNumber extends BullsAndCowsException {
        public InvalidNumber(String input) {
            super("Error: \"" + input + "\" isn't a valid number.");
        }
    }

    public static class InvalidSymbol extends BullsAndCowsException {
        public InvalidSymbol(String input) {
            super("Error: \"" + input + "\" contains invalid symbols.");
        }
    }

    public static class MoreSymbolsThanPossible extends BullsAndCowsException {
        public MoreSymbolsThanPossible() {
            super("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
        }
    }
}
