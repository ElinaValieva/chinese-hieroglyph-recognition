package MLP.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * author: ElinaValieva on 28.04.2019
 * Database constants
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Table {

    public static final String TABLE_NAME = "hyeroglyph";

    public static final String ID = "id";

    public static final String CODE = "code";

    public static final String TRANSCRIPTION = "transcription";

    public static final String TRANSLATION_EN = "translation_EN";

    public static final String TRANSLATION_RU = "translation_RU";

    public static final String KEY = "chinese_key";
}
