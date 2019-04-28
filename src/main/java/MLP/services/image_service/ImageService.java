package MLP.services.image_service;

import MLP.model.HieroglyphRecognitionModel;
import marvin.image.MarvinSegment;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * author: ElinaValieva on 28.04.2019
 */
public interface ImageService {

    int[][] imageToVector(BufferedImage bufferedImage);

    int[][] resizeVector(HieroglyphRecognitionModel hieroglyphRecognitionModel, MarvinSegment marvinSegment);

    BufferedImage vectorToImage(int[][] vector);

    BufferedImage getImage(String path);

    int[][] resize(int[][] vector);

    BufferedImage scale(BufferedImage imageToScale, int dWidth, int dHeight);

    void resing(HieroglyphRecognitionModel hieroglyphRecognitionModel);

    void resizeHieroglyphs(List<HieroglyphRecognitionModel> hieroglyphs);

    HieroglyphRecognitionModel mapToModel(String path);

    HieroglyphRecognitionModel mapToModel(int[][] vector);

    void mapToModel(HieroglyphRecognitionModel hieroglyphRecognitionModel, int[][] vector, BufferedImage bufferedImage, int width, int height);

    double[] generateVectorNN(HieroglyphRecognitionModel hieroglyphRecognitionModel);

}
