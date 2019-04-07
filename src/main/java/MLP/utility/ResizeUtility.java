package MLP.utility;

import lombok.NoArgsConstructor;

import java.util.stream.IntStream;

/**
 * author: ElinaValieva on 07.04.2019
 * Service for resizing images
 */
@NoArgsConstructor
public class ResizeUtility {

    private static final int RESIZE_X = 50;

    public static int[][] resize(int[][] vector) {
        int width = vector[0].length;
        int height = vector.length;
        return (width < RESIZE_X && height < RESIZE_X) ?
                resizeX(vector) :
                vector;
    }

    private static int[][] resizeX(int[][] vector) {
        int width = vector[0].length;
        int height = vector.length;
        int[][] resultVector = new int[RESIZE_X][RESIZE_X];
        IntStream.range(0, height).forEach(heightIndex ->
                IntStream.range(0, width).forEach(widthIndex ->
                        resultVector[heightIndex][widthIndex] = vector[heightIndex][widthIndex]
                ));

        return resultVector;
    }
}
