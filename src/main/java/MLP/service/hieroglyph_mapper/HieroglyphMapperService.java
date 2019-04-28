package MLP.service.hieroglyph_mapper;

import MLP.model.HieroglyphRecognitionModel;

import java.awt.image.BufferedImage;

/**
 * author: ElinaValieva on 28.04.2019
 */
public interface HieroglyphMapperService {

    HieroglyphRecognitionModel mapToModel(String path);

    HieroglyphRecognitionModel mapToModel(int[][] vector);

    void mapToModel(HieroglyphRecognitionModel hieroglyphRecognitionModel, int[][] vector, BufferedImage bufferedImage, int width, int height);

    double[] generateVectorNN(HieroglyphRecognitionModel hieroglyphRecognitionModel);

}
