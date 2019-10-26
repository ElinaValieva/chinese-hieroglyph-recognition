package MLP.service.segmentation.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.awt.*;

/**
 * author: ElinaValieva on 06.04.2019
 * Constants for segmentation
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SegmentationConstants {

    public static final Color COLOR_SEGMENTS = Color.BLUE;

    public static final int MATRIX_ROW_SIZE_X = 1;
    public static final int MATRIX_ROW_SIZE_Y = 1;
    public static final int THRESHOLD = 127;

    public static final String SEGMENTATION_RESULT_FILE_NAME = "segmentation.png";

    public static final int THRESHOLD_FOR_AREA = 75;
    public static final int THRESHOLD_FOR_X = 7;
    public static final int THRESHOLD_FOR_Y = 7;
}
