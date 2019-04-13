package MLP.services.preprocessing;

import MLP.utility.FileUtility;
import MLP.utility.ImageUtility;
import MLP.utility.ResizeUtility;
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

    private final FileUtility fileManagerService;
    private final ImageUtility imageUtility;
    private final ResizeUtility resizeUtility;

    @Autowired
    public FilterService(FileUtility fileManagerService, ImageUtility imageUtility, ResizeUtility resizeUtility) {
        this.fileManagerService = fileManagerService;
        this.imageUtility = imageUtility;
        this.resizeUtility = resizeUtility;
    }

    public void filter(String imagePath) throws IOException {
        BufferedImage bufferedImage = imageUtility.getImage(imagePath);
        bufferedImage = resizeUtility.scale(bufferedImage, bufferedImage.getWidth(), bufferedImage.getHeight());
        int[][] vector = imageUtility.imageToVector(bufferedImage);
        bufferedImage = imageUtility.vectorToImage(vector);
        fileManagerService.saveImage(bufferedImage, imagePath);
    }
}
