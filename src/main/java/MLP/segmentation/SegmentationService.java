package MLP.segmentation;


import MLP.exception.ErrorCode;
import MLP.exception.RecognitionException;
import marvin.color.MarvinColorModelConverter;
import marvin.image.MarvinImage;
import marvin.image.MarvinSegment;
import marvin.io.MarvinImageIO;
import marvin.math.MarvinMath;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

import static MLP.segmentation.common.SegmentationConstants.*;
import static marvinplugins.MarvinPluginCollection.floodfillSegmentation;
import static marvinplugins.MarvinPluginCollection.morphologicalClosing;

/**
 * Service for segmentation hieroglyph for simple codes
 * Usage: Marvin Framework
 * author: ElinaValieva on 06.04.2019
 */
@Service
public class SegmentationService {

    public void segment(String imagePath) throws RecognitionException {
        MarvinImage loadImage = MarvinImageIO.loadImage(imagePath);

        if (loadImage == null)
            throw new RecognitionException(ErrorCode.ERROR_CODE_FILE_NOT_FOUND.getMessage());

        MarvinImage image = loadImage.clone();
        filterGreen(image);
        MarvinImage marvinImage = MarvinColorModelConverter.rgbToBinary(image, THRESHOLD);
        morphologicalClosing(marvinImage.clone(), marvinImage, MarvinMath.getTrueMatrix(MATRIX_ROW_SIZE_X, MATRIX_ROW_SIZE_Y));
        image = MarvinColorModelConverter.binaryToRgb(marvinImage);
        MarvinSegment[] segments = floodfillSegmentation(image);

        IntStream.range(1, segments.length).forEach(i -> {
            MarvinSegment segmentResult = segments[i];
            loadImage.drawRect(segmentResult.x1, segmentResult.y1, segmentResult.width, segmentResult.height, COLOR_SEGMENTS);
        });
        MarvinImageIO.saveImage(loadImage, "C:/Users/Elina/Desktop/result.png");
    }

    private void filterGreen(MarvinImage image) {
        final int[] redColor = new int[1];
        final int[] greenColor = new int[1];
        final int[] blueColor = new int[1];
        IntStream.range(0, image.getHeight()).forEach(height ->
                IntStream.range(0, image.getWidth()).forEach(width -> {
                    redColor[0] = image.getIntComponent0(width, height);
                    greenColor[0] = image.getIntComponent1(width, height);
                    blueColor[0] = image.getIntComponent2(width, height);
                    if (greenColor[0] > redColor[0] * 1.5 && greenColor[0] > blueColor[0] * 1.5)
                        image.setIntColor(width, height, 255, 255, 255);
                })
        );
    }
}
