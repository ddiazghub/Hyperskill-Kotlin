package bullscows;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Game {
    private final Set<Character> allowed;
    private final String code;
    private final Map<Character, Integer> indexes;

    public Game(int codeLength, int symbols) {
        this.allowed = IntStream.range(0, symbols).boxed()
            .map(it -> it < 10 ? (char) ('0' + it) : (char) ('a' + it - 10))
            .collect(Collectors.toUnmodifiableSet());

        List<Character> allowedSymbols = new ArrayList<>(allowed);
        Collections.shuffle(allowedSymbols);

        this.code = allowedSymbols.stream()
                .limit(codeLength)
                .collect(Collector.of(StringBuilder::new, StringBuilder::append, StringBuilder::append, StringBuilder::toString));

        this.indexes = new HashMap<>();

        for (int i = 0; i < code.length(); i++) {
            this.indexes.put(code.charAt(i), i);
        }
    }

    public Grade check(String guess) {
        int bulls = 0;
        int cows = 0;

        for (int i = 0; i < guess.length(); i++) {
            char ch = guess.charAt(i);

            if (!allowed.contains(ch))
                throw new BullsAndCowsException.InvalidSymbol(guess);

            int index = indexes.getOrDefault(ch, -1);

            if (index == i) bulls++;
            else if (index > -1) cows++;
        }

        return new Grade(bulls, cows);
    }
}
