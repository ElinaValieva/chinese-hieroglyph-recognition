package MLP.service.hieroglyph_mapper;

import MLP.model.HieroglyphRecognitionModel;
import MLP.service.image_manager.ImageService;
import MLP.utility.PathGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

/**
 * author: ElinaValieva on 28.04.2019
 */
@Service
public class HieroglyphMapperManagerService implements HieroglyphMapperService {

    private final ImageService imageService;

    @Autowired
    public HieroglyphMapperManagerService(ImageService imageService) {
        this.imageService = imageService;
    }

    public HieroglyphRecognitionModel mapToModel(String path) {
        BufferedImage bufferedImage = imageService.getImage(path);
        int[][] imageVector = imageService.imageToVector(bufferedImage);
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
        BufferedImage bufferedImage = imageService.vectorToImage(vector);
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
}
