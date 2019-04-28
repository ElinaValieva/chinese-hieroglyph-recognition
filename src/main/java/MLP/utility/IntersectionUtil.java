package MLP.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import marvin.image.MarvinSegment;
import marvin.math.Point;

import java.util.Arrays;
import java.util.stream.IntStream;

import static MLP.service.segmentation.common.SegmentationConstants.*;

/**
 * author: ElinaValieva on 28.04.2019
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IntersectionUtil {

    private static boolean intersection(Point pointAStart, Point pointBStart, Point pointBEnd) {
        return pointBStart.x <= pointAStart.x &&
                pointAStart.x <= pointBEnd.x &&
                pointBStart.y <= pointAStart.y &&
                pointAStart.y <= pointBEnd.y;
    }

    public static boolean innerIntersection(MarvinSegment marvinSegment, MarvinSegment marvinSegmentSecond) {
        Point pointAStart = new Point(marvinSegment.x1, marvinSegment.y1);
        Point pointAEnd = new Point(marvinSegment.x2, marvinSegment.y2);
        Point pointBStart = new Point(marvinSegmentSecond.x1, marvinSegmentSecond.y1);
        Point pointBEnd = new Point(marvinSegmentSecond.x2, marvinSegmentSecond.y2);

        return (intersection(pointAStart, pointBStart, pointBEnd) &&
                intersection(pointAEnd, pointBStart, pointBEnd));
    }

    public static boolean isEmptyArea(int[][] vector) {
        int height = vector.length;
        int[] resultList = new int[height];
        int thresholdHeight = Math.toIntExact(Math.round(height * 0.5));
        IntStream.range(0, height).forEach(heightIndex ->
                resultList[heightIndex] = Arrays.stream(vector[heightIndex]).max().getAsInt());

        long cntEmptyElement = Arrays.stream(resultList).filter(element -> element == 0).count();
        return cntEmptyElement >= thresholdHeight;
    }

    public static boolean isAvailableArea(MarvinSegment marvinSegment) {
        return marvinSegment.area <= THRESHOLD_FOR_AREA || marvinSegment.height <= THRESHOLD_FOR_Y || marvinSegment.width <= THRESHOLD_FOR_X;
    }
}
