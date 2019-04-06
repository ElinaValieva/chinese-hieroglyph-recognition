package MLP.utility;

import MLP.model.HieroglyphRecognitionModel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import marvin.image.MarvinSegment;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;

/**
 * author: ElinaValieva on 06.04.2019
 * Utility for converting image to vector and vector to image
 */
@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageUtility {

    static int[][] imageToVector(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        BufferedImage bufferedProcessingImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bufferedProcessingImage.createGraphics();
        graphics.drawImage(bufferedImage, 0, 0, null);
        graphics.dispose();
        int[][] outputResult = new int[height][width];
        IntStream.range(0, width).forEach(widthIndex ->
                IntStream.range(0, height).forEach(heightIndex -> {
                    int[] d = new int[3];
                    bufferedProcessingImage.getRaster().getPixel(widthIndex, heightIndex, d);
                    if (d[0] > 128)
                        outputResult[heightIndex][widthIndex] = 0;
                    else
                        outputResult[heightIndex][widthIndex] = 1;
                })
        );
        return outputResult;
    }

    private static int[][] resizeVector(int[][] vector, Point startPoint, int width, int height) {
        int[][] outputResult = new int[height][width];
        int startPointX = startPoint.x;
        int startPointY = startPoint.y;
        IntStream.range(0, height).forEach(y ->
                IntStream.range(0, width).forEach(x -> outputResult[y][x] = vector[startPointY + y][startPointX + x]));

        return outputResult;
    }

    public static int[][] resizeVector(HieroglyphRecognitionModel hieroglyphRecognitionModel, MarvinSegment marvinSegment) {
        Point startPoint = new Point(marvinSegment.x1, marvinSegment.y1);
        return resizeVector(hieroglyphRecognitionModel.getVector(), startPoint, marvinSegment.width, marvinSegment.height);
    }

    static BufferedImage vectorToImage(int[][] vector) {
        int width = vector.length;
        int height = vector[0].length;
        BufferedImage bufferedImage = new BufferedImage(height, width, BufferedImage.TYPE_BYTE_BINARY);

        IntStream.range(0, width).forEach(widthIndex ->
                IntStream.range(0, height).forEach(heightIndex -> {
                    int rgb = vector[widthIndex][heightIndex] == 1 ? Color.BLACK.getRGB() : Color.WHITE.getRGB();
                    bufferedImage.setRGB(heightIndex, widthIndex, rgb);
                })
        );

        return bufferedImage;
    }

    public static BufferedImage getImage(String path) throws IOException {
        return ImageIO.read(new File(path));
    }
}
