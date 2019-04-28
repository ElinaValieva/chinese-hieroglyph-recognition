package MLP.service.resizing;

import MLP.model.HieroglyphRecognitionModel;
import MLP.service.hieroglyph_mapper.HieroglyphMapperService;
import MLP.service.image_manager.ImageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

/**
 * author: ElinaValieva on 28.04.2019
 */
@Log4j2
@Service
public class ResizeManagerService implements ResizeService {

    private static final int RESIZE_X = 100;

    private final HieroglyphMapperService hieroglyphMapperService;
    private final ImageService imageService;

    @Autowired
    public ResizeManagerService(HieroglyphMapperService hieroglyphMapperService, ImageService imageService) {
        this.hieroglyphMapperService = hieroglyphMapperService;
        this.imageService = imageService;
    }

    private int[][] resizeX(int[][] vector) {
        int width = vector[0].length;
        int height = vector.length;
        int[][] resultVector = new int[RESIZE_X][RESIZE_X];
        IntStream.range(0, height).forEach(heightIndex ->
                IntStream.range(0, width).forEach(widthIndex ->
                        resultVector[heightIndex][widthIndex] = vector[heightIndex][widthIndex]
                ));

        return resultVector;
    }

    private void resizeBySize(HieroglyphRecognitionModel hieroglyphRecognitionModel, int RESIZE_X) {
        int[][] vector = hieroglyphRecognitionModel.getVector();
        int width = vector[0].length;
        int height = vector.length;
        int[][] resultVector = new int[RESIZE_X][RESIZE_X];
        IntStream.range(0, height).forEach(heightIndex ->
                IntStream.range(0, width).forEach(widthIndex ->
                        resultVector[heightIndex][widthIndex] = vector[heightIndex][widthIndex]
                ));
        HieroglyphRecognitionModel.builder(hieroglyphRecognitionModel)
                .vector(resultVector);
    }

    @Override
    public BufferedImage scale(BufferedImage imageToScale, int dWidth, int dHeight) {
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

    private void resizeBySquare(HieroglyphRecognitionModel hieroglyphRecognitionModel) {
        int[][] vector = hieroglyphRecognitionModel.getVector();
        BufferedImage bufferedImage = imageService.vectorToImage(vector);
        bufferedImage = scale(bufferedImage, RESIZE_X, RESIZE_X);
        int[][] resultVector = imageService.imageToVector(bufferedImage);
        try {
            hieroglyphMapperService.mapToModel(hieroglyphRecognitionModel, resultVector, bufferedImage, RESIZE_X, RESIZE_X);
        } catch (IOException e) {
            log.warn("Couldn't save image");
        }
    }

    @Override
    public void resizing(HieroglyphRecognitionModel hieroglyphRecognitionModel) {
        int[][] vector = hieroglyphRecognitionModel.getVector();
        int width = vector[0].length;
        int height = vector.length;
        int currentSize = width > height ? width : height;
        resizeBySize(hieroglyphRecognitionModel, currentSize);
        resizeBySquare(hieroglyphRecognitionModel);
    }

    @Override
    public void resizeHieroglyphs(List<HieroglyphRecognitionModel> hieroglyphs) {
        hieroglyphs.forEach(this::resizing);
    }

}
