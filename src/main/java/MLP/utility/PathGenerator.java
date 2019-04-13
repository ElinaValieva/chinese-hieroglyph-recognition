package MLP.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * author: ElinaValieva on 13.04.2019
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PathGenerator {

    private static Integer number = 0;

    public static String getPath() {
        number++;
        return String.format("result_%s.png", number);
    }
}
