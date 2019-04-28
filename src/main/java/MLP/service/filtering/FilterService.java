package MLP.service.filtering;

import MLP.service.file_manager.FileService;
import MLP.service.image_manager.ImageService;
import MLP.service.resizing.ResizeService;
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
    private final ResizeService resizeService;
    private final ImageService imageService;

    @Autowired
    public FilterService(FileService fileService, ResizeService resizeService, ImageService imageService) {
        this.fileService = fileService;
        this.resizeService = resizeService;
        this.imageService = imageService;
    }

    public void filter(String imagePath) throws IOException {
        BufferedImage bufferedImage = imageService.getImage(imagePath);
        bufferedImage = resizeService.scale(bufferedImage, bufferedImage.getWidth(), bufferedImage.getHeight());
        int[][] vector = imageService.imageToVector(bufferedImage);
        bufferedImage = imageService.vectorToImage(vector);
        fileService.saveImage(bufferedImage, imagePath);
    }
}
