package MLP.utility;

import MLP.model.HieroglyphRecognitionModel;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.IntStream;

/**
 * author: ElinaValieva on 07.04.2019
 * Service for resizing images
 */
@Component
public class ResizeUtility {

    private static final int RESIZE_X = 100;

    private final ImageUtility imageUtility;

    private final RecognitionModelMapUtility recognitionModelMapUtility;


    public ResizeUtility(ImageUtility imageUtility, RecognitionModelMapUtility recognitionModelMapUtility) {
        this.imageUtility = imageUtility;
        this.recognitionModelMapUtility = recognitionModelMapUtility;
    }

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
        BufferedImage bufferedImage = imageUtility.vectorToImage(vector);
        bufferedImage = scale(bufferedImage, RESIZE_X, RESIZE_X);
        int[][] resultVector = imageUtility.imageToVector(bufferedImage);
        recognitionModelMapUtility.mapToModel(hieroglyphRecognitionModel, resultVector, bufferedImage, RESIZE_X, RESIZE_X);
    }

    public void resing(HieroglyphRecognitionModel hieroglyphRecognitionModel) {
        int[][] vector = hieroglyphRecognitionModel.getVector();
        int width = vector[0].length;
        int height = vector.length;
        int currentSize = width > height ? width : height;
        resizeBySize(hieroglyphRecognitionModel, currentSize);
        resizeBySquare(hieroglyphRecognitionModel);
    }

    public void resizeHieroglyphs(List<HieroglyphRecognitionModel> hieroglyphs) {
        hieroglyphs.forEach(this::resing);
    }
}
