package MLP.configs;

import java.util.HashMap;
import java.util.Map;

/**
 * author: ElinaValieva on 23.12.2018
 */

public class DictionaryConfig {

    private static DictionaryConfig dictionaryConfig;
    private static Map<Integer, String> dictionary = new HashMap<>();

    private DictionaryConfig() {
        dictionary.put(1, "флейта");
        dictionary.put(2, "дракон");
        dictionary.put(3, "черепаха");
        dictionary.put(4, "звук");
        dictionary.put(5, "видеть");
        dictionary.put(6, "лодка");
        dictionary.put(7, "сладкий");
        dictionary.put(8, "темный");
        dictionary.put(9, "бамбук");
    }

    public static DictionaryConfig getDictionaryConfig() {
        if (dictionaryConfig == null)
            dictionaryConfig = new DictionaryConfig();
        return dictionaryConfig;
    }

    public String getResult(Integer code) {
        return dictionary.get(code);
    }
}
