package core.basesyntax.db;

import java.util.HashMap;
import java.util.Map;

public class Storage {
    private static Map<String, Integer> calculatedTransactions = new HashMap<>();

    public static Map<String, Integer> getCalculatedTransactions() {
        return calculatedTransactions;
    }
}
