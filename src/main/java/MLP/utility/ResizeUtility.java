package MLP.utility;

import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

/**
 * author: ElinaValieva on 07.04.2019
 * Service for resizing images
 */
@Component
public class ResizeUtility {

    private static final int RESIZE_X = 50;

    public int[][] resize(int[][] vector) {
        int width = vector[0].length;
        int height = vector.length;
        return (width < RESIZE_X && height < RESIZE_X) ?
                resizeX(vector) :
                vector;
    }

    private int[][] resizeX(int[][] vector) {
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
