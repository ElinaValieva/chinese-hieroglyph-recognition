package MLP.services.segmentation;


import MLP.exception.ErrorCode;
import MLP.exception.RecognitionException;
import MLP.model.HieroglyphRecognitionModel;
import MLP.utility.FileUtility;
import MLP.utility.ImageUtility;
import MLP.utility.RecognitionModelMapUtility;
import MLP.utility.ResizeUtility;
import lombok.extern.log4j.Log4j2;
import marvin.color.MarvinColorModelConverter;
import marvin.image.MarvinImage;
import marvin.image.MarvinSegment;
import marvin.io.MarvinImageIO;
import marvin.math.MarvinMath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static MLP.services.segmentation.common.SegmentationConstants.*;
import static marvinplugins.MarvinPluginCollection.floodfillSegmentation;
import static marvinplugins.MarvinPluginCollection.morphologicalClosing;

/**
 * Service for segmentation hieroglyph for simple codes
 * Usage: Marvin Framework
 * author: ElinaValieva on 06.04.2019
 */
@Log4j2
@Service
public class SegmentationService {

    private final FileUtility fileManagerService;
    private final ImageUtility imageUtility;
    private final RecognitionModelMapUtility recognitionModelMapUtility;
    private final ResizeUtility resizeUtility;

    private List<HieroglyphRecognitionModel> hieroglyphRecognitionModels;

    @Autowired
    public SegmentationService(FileUtility fileManagerService,
                               ImageUtility imageUtility,
                               RecognitionModelMapUtility recognitionModelMapUtility,
                               ResizeUtility resizeUtility) {
        this.fileManagerService = fileManagerService;
        this.imageUtility = imageUtility;
        this.recognitionModelMapUtility = recognitionModelMapUtility;
        this.resizeUtility = resizeUtility;
    }

    public List<HieroglyphRecognitionModel> segment(String imagePath) throws RecognitionException, IOException {
        log.debug("Start segmenting process for image: {}", imagePath);
        MarvinImage loadImage = MarvinImageIO.loadImage(imagePath);

        if (loadImage == null)
            throw new RecognitionException(ErrorCode.ERROR_CODE_FILE_NOT_FOUND.getMessage());

        HieroglyphRecognitionModel hieroglyphRecognitionModel = recognitionModelMapUtility.mapToModel(imagePath);
        hieroglyphRecognitionModels = new ArrayList<>();

        MarvinImage image = loadImage.clone();
        filterGreen(image);
        MarvinImage marvinImage = MarvinColorModelConverter.rgbToBinary(image, THRESHOLD);
        morphologicalClosing(marvinImage.clone(), marvinImage, MarvinMath.getTrueMatrix(MATRIX_ROW_SIZE_X, MATRIX_ROW_SIZE_Y));
        image = MarvinColorModelConverter.binaryToRgb(marvinImage);
        MarvinSegment[] segments = floodfillSegmentation(image);

        IntStream.range(1, segments.length).forEach(i -> {
            MarvinSegment segmentResult = segments[i];
            formResult(hieroglyphRecognitionModel, segmentResult, loadImage);
        });

        MarvinImageIO.saveImage(loadImage, fileManagerService.getFileResourceDirectory(SEGMENTATION_RESULT_FILE_NAME));
        return hieroglyphRecognitionModels;
    }

    private void filterGreen(MarvinImage image) {
        IntStream.range(0, image.getHeight()).forEach(height ->
                IntStream.range(0, image.getWidth()).forEach(width -> {
                    int redColor = image.getIntComponent0(width, height);
                    int greenColor = image.getIntComponent1(width, height);
                    int blueColor = image.getIntComponent2(width, height);
                    if (greenColor > redColor * 1.5 && greenColor > blueColor * 1.5)
                        image.setIntColor(width, height, 255, 255, 255);
                })
        );
    }

    private boolean isAvailable(MarvinSegment marvinSegment) {
        return marvinSegment.area > THRESHOLD_FOR_RESING;
    }

    private void formResult(HieroglyphRecognitionModel hieroglyphRecognitionModel, MarvinSegment segmentResult, MarvinImage loadImage) {
        if (!isAvailable(segmentResult))
            return;

        int[][] resizingVector = imageUtility.resizeVector(hieroglyphRecognitionModel, segmentResult);

        if (!isNotEmptyImage(resizingVector))
            return;

        resizingVector = resizeUtility.resize(resizingVector);
        HieroglyphRecognitionModel hieroglyphResizingRecognitionModel = recognitionModelMapUtility.mapToModel(resizingVector);
        hieroglyphRecognitionModels.add(hieroglyphResizingRecognitionModel);
        loadImage.drawRect(segmentResult.x1, segmentResult.y1, segmentResult.width, segmentResult.height, COLOR_SEGMENTS);
    }

    private boolean isNotEmptyImage(int[][] vector) {
        int height = vector.length;
        int[] resultList = new int[height];
        int thresholdHeight = Math.toIntExact(Math.round(height * 0.5));
        IntStream.range(0, height).forEach(heightIndex ->
                resultList[heightIndex] = Arrays.stream(vector[heightIndex]).max().getAsInt()
        );

        long cntEmptyElement = Arrays.stream(resultList).filter(element -> element == 0).count();
        return cntEmptyElement < thresholdHeight;
    }
}
