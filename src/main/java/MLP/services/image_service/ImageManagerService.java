package MLP.services.image_service;

import MLP.model.HieroglyphRecognitionModel;
import MLP.utility.PathGenerator;
import lombok.extern.log4j.Log4j2;
import marvin.image.MarvinSegment;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

/**
 * author: ElinaValieva on 06.04.2019
 * Utility for converting image to vector and vector to image
 */
@Log4j2
@Service
public class ImageManagerService implements ImageService {

    private static final int RESIZE_X = 100;

    @Override
    public int[][] imageToVector(BufferedImage bufferedImage) {
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

    private int[][] resizeVector(int[][] vector, Point startPoint, int width, int height) {
        int[][] outputResult = new int[height][width];
        int startPointX = startPoint.x;
        int startPointY = startPoint.y;
        IntStream.range(0, height).forEach(y ->
                IntStream.range(0, width).forEach(x -> outputResult[y][x] = vector[startPointY + y][startPointX + x]));

        return outputResult;
    }

    @Override
    public int[][] resizeVector(HieroglyphRecognitionModel hieroglyphRecognitionModel, MarvinSegment marvinSegment) {
        Point startPoint = new Point(marvinSegment.x1, marvinSegment.y1);
        return resizeVector(hieroglyphRecognitionModel.getVector(), startPoint, marvinSegment.width, marvinSegment.height);
    }

    @Override
    public BufferedImage vectorToImage(int[][] vector) {
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

    @Override
    public BufferedImage getImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            log.error("", e);
            return null;
        }
    }

    @Override
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

    private void resizeBySize(HieroglyphRecognitionModel hieroglyphRecognitionModel, int RESIZE_X) {
        int[][] vector = hieroglyphRecognitionModel.getVector();
        int width = vector[0].length;
        int height = vector.length;
        int[][] resultVector = new int[RESIZE_X][RESIZE_X];
        IntStream.range(0, height).forEach(heightIndex ->
                IntStream.range(0, width).forEach(widthIndex ->
                        resultVector[heightIndex][widthIndex] = vector[heightIndex][widthIndex]
                ));
        HieroglyphRecognitionModel.builder(hieroglyphRecognitionModel)
                .vector(resultVector);
    }

    @Override
    public BufferedImage scale(BufferedImage imageToScale, int dWidth, int dHeight) {
        BufferedImage scaledImage = null;
        if (imageToScale != null) {
            scaledImage = new BufferedImage(dWidth, dHeight, BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D graphics2D = scaledImage.createGraphics();
            graphics2D.drawImage(imageToScale, 0, 0, dWidth, dHeight, null);
            graphics2D.dispose();
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        return scaledImage;
    }

    private void resizeBySquare(HieroglyphRecognitionModel hieroglyphRecognitionModel) {
        int[][] vector = hieroglyphRecognitionModel.getVector();
        BufferedImage bufferedImage = vectorToImage(vector);
        bufferedImage = scale(bufferedImage, RESIZE_X, RESIZE_X);
        int[][] resultVector = imageToVector(bufferedImage);
        mapToModel(hieroglyphRecognitionModel, resultVector, bufferedImage, RESIZE_X, RESIZE_X);
    }

    @Override
    public void resing(HieroglyphRecognitionModel hieroglyphRecognitionModel) {
        int[][] vector = hieroglyphRecognitionModel.getVector();
        int width = vector[0].length;
        int height = vector.length;
        int currentSize = width > height ? width : height;
        resizeBySize(hieroglyphRecognitionModel, currentSize);
        resizeBySquare(hieroglyphRecognitionModel);
    }

    @Override
    public void resizeHieroglyphs(List<HieroglyphRecognitionModel> hieroglyphs) {
        hieroglyphs.forEach(this::resing);
    }

    public HieroglyphRecognitionModel mapToModel(String path) {
        BufferedImage bufferedImage = getImage(path);
        int[][] imageVector = imageToVector(bufferedImage);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        return HieroglyphRecognitionModel.builder()
                .bufferedImage(bufferedImage)
                .vector(imageVector)
                .height(height)
                .path(path)
                .width(width)
                .build();
    }

    public HieroglyphRecognitionModel mapToModel(int[][] vector) {
        BufferedImage bufferedImage = vectorToImage(vector);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        String path = PathGenerator.getPath();
        return HieroglyphRecognitionModel.builder()
                .bufferedImage(bufferedImage)
                .vector(vector)
                .height(height)
                .path(path)
                .width(width)
                .build();
    }

    public void mapToModel(HieroglyphRecognitionModel hieroglyphRecognitionModel, int[][] vector, BufferedImage bufferedImage, int width, int height) {
        HieroglyphRecognitionModel.builder(hieroglyphRecognitionModel)
                .bufferedImage(bufferedImage)
                .vector(vector)
                .height(height)
                .width(width);
    }

    private double[] generateVectorNN(int[][] vector, int width, int height) {
        double[] inputs = new double[width * height];
        for (int i = 0; i < vector.length; i++) {
            int[] row = vector[i];
            for (int j = 0; j < row.length; j++) {
                int number = vector[i][j];
                inputs[i * row.length + j] = Double.valueOf(number);
            }
        }
        return inputs;
    }

    public double[] generateVectorNN(HieroglyphRecognitionModel hieroglyphRecognitionModel) {
        int[][] vector = hieroglyphRecognitionModel.getVector();
        int w = hieroglyphRecognitionModel.getWidth();
        int h = hieroglyphRecognitionModel.getHeight();
        return generateVectorNN(vector, w, h);
    }
}
