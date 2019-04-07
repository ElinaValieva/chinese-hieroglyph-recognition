package MLP.utility;

import MLP.model.HieroglyphRecognitionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.IOException;

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

    public HieroglyphRecognitionModel mapToModel(String path) throws IOException {
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
}
