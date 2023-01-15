package client.http.util;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class JsonEntityLinePredicate implements Predicate<String> {

    private static final Set<String> JSON_OBJECT_SYMBOLS = Arrays.stream("{}[]\":".split("")).collect(Collectors.toSet());

    @Override
    public boolean test(String line) {
        for (String jsonObjectSymbol : JSON_OBJECT_SYMBOLS)
            if (line.contains(jsonObjectSymbol)) return true;
        return false;
    }
}
