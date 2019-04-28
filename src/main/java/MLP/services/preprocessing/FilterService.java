package MLP.services.preprocessing;

import MLP.services.file_service.FileService;
import MLP.services.image_service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * author: ElinaValieva on 07.04.2019
 * Service for improving image: noise removal, filtering, smoothing
 */
@Service
public class FilterService {

    private final FileService fileService;
    private final ImageService imageService;

    @Autowired
    public FilterService(FileService fileService,
                         ImageService imageService) {
        this.fileService = fileService;
        this.imageService = imageService;
    }

    public void filter(String imagePath) throws IOException {
        BufferedImage bufferedImage = imageService.getImage(imagePath);
        bufferedImage = imageService.scale(bufferedImage, bufferedImage.getWidth(), bufferedImage.getHeight());
        int[][] vector = imageService.imageToVector(bufferedImage);
        bufferedImage = imageService.vectorToImage(vector);
        fileService.saveImage(bufferedImage, imagePath);
    }
}
