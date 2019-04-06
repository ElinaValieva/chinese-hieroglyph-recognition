package MLP.utility;

import MLP.model.HieroglyphRecognitionModel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import marvin.image.MarvinImage;

import java.awt.image.BufferedImage;

/**
 * author: ElinaValieva on 06.04.2019
 * HieroglyphRecognitionModel mapping utility
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RecognitionModelMapUtility {

    public static HieroglyphRecognitionModel mapToModel(MarvinImage marvinImage) {
        BufferedImage bufferedImage = marvinImage.getBufferedImage();
        int[][] imageVector = ImageUtility.imageToVector(bufferedImage);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        return HieroglyphRecognitionModel.builder()
                .bufferedImage(bufferedImage)
                .vector(imageVector)
                .height(height)
                .width(width)
                .build();
    }

    public static HieroglyphRecognitionModel mapToModel(int[][] vector){
        BufferedImage bufferedImage = ImageUtility.vectorToImage(vector);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        return HieroglyphRecognitionModel.builder()
                .bufferedImage(bufferedImage)
                .vector(vector)
                .height(height)
                .width(width)
                .build();
    }
}
