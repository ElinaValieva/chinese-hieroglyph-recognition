package MLP.service.resizing;

import MLP.model.HieroglyphRecognitionModel;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * author: ElinaValieva on 28.04.2019
 */
public interface ResizeService {

    BufferedImage scale(BufferedImage imageToScale, int dWidth, int dHeight);

    void resizing(HieroglyphRecognitionModel hieroglyphRecognitionModel) throws IOException;

    void resizeHieroglyphs(List<HieroglyphRecognitionModel> hieroglyphs);
}
