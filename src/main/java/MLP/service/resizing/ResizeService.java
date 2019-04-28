package MLP.service.resizing;

import MLP.model.HieroglyphRecognitionModel;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * author: ElinaValieva on 28.04.2019
 */
public interface ResizeService {

    int[][] resize(int[][] vector);

    BufferedImage scale(BufferedImage imageToScale, int dWidth, int dHeight);

    void resing(HieroglyphRecognitionModel hieroglyphRecognitionModel);

    void resizeHieroglyphs(List<HieroglyphRecognitionModel> hieroglyphs);
}
