package MLP.services.preprocessing;

import MLP.utility.FileUtility;
import MLP.utility.ImageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
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

    @Autowired
    public FilterService(FileUtility fileManagerService, ImageUtility imageUtility) {
        this.fileManagerService = fileManagerService;
        this.imageUtility = imageUtility;
    }

    public void filter(String imagePath) throws IOException {
        BufferedImage bufferedImage = imageUtility.getImage(imagePath);
        bufferedImage = scale(bufferedImage, bufferedImage.getWidth(), bufferedImage.getHeight());
        int[][] vector = imageUtility.imageToVector(bufferedImage);
        bufferedImage = imageUtility.vectorToImage(vector);
        fileManagerService.saveImage(bufferedImage, imagePath);
    }

    private BufferedImage scale(BufferedImage imageToScale, int dWidth, int dHeight) {
        BufferedImage scaledImage = null;
        if (imageToScale != null) {
            scaledImage = new BufferedImage(dWidth, dHeight, BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D graphics2D = scaledImage.createGraphics();
            graphics2D.drawImage(imageToScale, 0, 0, dWidth, dHeight, null);
            graphics2D.dispose();
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        return scaledImage;
    }
}
