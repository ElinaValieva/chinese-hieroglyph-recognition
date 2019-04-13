package MLP.utility;

import MLP.model.HieroglyphRecognitionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

/**
 * author: ElinaValieva on 06.04.2019
 * HieroglyphRecognitionModel mapping utility
 */
@Component
public class RecognitionModelMapUtility {

    private final ImageUtility imageUtility;

    @Autowired
    public RecognitionModelMapUtility(ImageUtility imageUtility) {
        this.imageUtility = imageUtility;
    }

    public HieroglyphRecognitionModel mapToModel(String path) {
        BufferedImage bufferedImage = imageUtility.getImage(path);
        int[][] imageVector = imageUtility.imageToVector(bufferedImage);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        return HieroglyphRecognitionModel.builder()
                .bufferedImage(bufferedImage)
                .vector(imageVector)
                .height(height)
                .width(width)
                .build();
    }

    public HieroglyphRecognitionModel mapToModel(int[][] vector) {
        BufferedImage bufferedImage = imageUtility.vectorToImage(vector);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        return HieroglyphRecognitionModel.builder()
                .bufferedImage(bufferedImage)
                .vector(vector)
                .height(height)
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
