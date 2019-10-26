package MLP.service.image_manager;

import MLP.model.HieroglyphRecognitionModel;
import marvin.image.MarvinSegment;

import java.awt.image.BufferedImage;

/**
 * author: ElinaValieva on 28.04.2019
 */
public interface ImageService {

    int[][] imageToVector(BufferedImage bufferedImage);

    int[][] resizeVector(HieroglyphRecognitionModel hieroglyphRecognitionModel, MarvinSegment marvinSegment);

    BufferedImage vectorToImage(int[][] vector);

    BufferedImage getImage(String path);
}
