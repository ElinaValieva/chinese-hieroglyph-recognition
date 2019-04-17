package MLP.services.segmentation;


import MLP.exception.ErrorCode;
import MLP.exception.RecognitionException;
import MLP.model.HieroglyphRecognitionModel;
import MLP.utility.FileUtility;
import MLP.utility.ImageUtility;
import MLP.utility.RecognitionModelMapUtility;
import lombok.extern.log4j.Log4j2;
import marvin.color.MarvinColorModelConverter;
import marvin.image.MarvinImage;
import marvin.image.MarvinSegment;
import marvin.io.MarvinImageIO;
import marvin.math.MarvinMath;
import marvin.math.Point;
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

    private final FileUtility fileUtility;
    private final ImageUtility imageUtility;
    private final RecognitionModelMapUtility recognitionModelMapUtility;

    private List<HieroglyphRecognitionModel> hieroglyphRecognitionModels;

    @Autowired
    public SegmentationService(FileUtility fileUtility,
                               ImageUtility imageUtility,
                               RecognitionModelMapUtility recognitionModelMapUtility) {
        this.fileUtility = fileUtility;
        this.imageUtility = imageUtility;
        this.recognitionModelMapUtility = recognitionModelMapUtility;
    }

    public List<HieroglyphRecognitionModel> segment(String imagePath) throws RecognitionException, IOException {
        log.debug("Start segmenting process for image: {}", imagePath);
        HieroglyphRecognitionModel hieroglyphRecognitionModel = recognitionModelMapUtility.mapToModel(imagePath);
        hieroglyphRecognitionModels = new ArrayList<>();

        MarvinImage loadImage = MarvinImageIO.loadImage(imagePath);

        if (loadImage == null)
            throw new RecognitionException(ErrorCode.ERROR_CODE_FILE_NOT_FOUND.getMessage());

        MarvinSegment[] segments = createSegments(loadImage);

        IntStream.range(1, segments.length).forEach(i -> {
            MarvinSegment segmentResult = segments[i];
            MarvinSegment segmentResultInnerElement = i < (segments.length - 1) ? segments[i + 1] : null;
            formResult(segmentResult, segmentResultInnerElement, hieroglyphRecognitionModel, loadImage);
        });

        MarvinImageIO.saveImage(loadImage, fileUtility.getPathDirectory(SEGMENTATION_RESULT_FILE_NAME));
        saveResults(hieroglyphRecognitionModels);
        return hieroglyphRecognitionModels;
    }

    private MarvinSegment[] createSegments(MarvinImage loadImage) {
        MarvinImage image = loadImage.clone();
        filterGreen(image);
        MarvinImage marvinImage = MarvinColorModelConverter.rgbToBinary(image, THRESHOLD);
        morphologicalClosing(marvinImage.clone(), marvinImage, MarvinMath.getTrueMatrix(MATRIX_ROW_SIZE_X, MATRIX_ROW_SIZE_Y));
        image = MarvinColorModelConverter.binaryToRgb(marvinImage);
        return floodfillSegmentation(image);
    }

    private void clearSegments(List<HieroglyphRecognitionModel> hieroglyphRecognitionModels) {
        hieroglyphRecognitionModels.forEach(this::clearSegment);
    }

    private void clearSegment(HieroglyphRecognitionModel hieroglyphRecognitionModel) {
        String path = fileUtility.getFilesPath(String.format("static/file-repository/%s", hieroglyphRecognitionModel.getPath()));
        MarvinImage loadImage = MarvinImageIO.loadImage(path);

        MarvinSegment[] segments = createSegments(loadImage);

        if (segments == null)
            return;

        if (segments.length > 1)
            IntStream.range(1, segments.length).forEach(segmentIndex ->
                    clearSegmentArea(hieroglyphRecognitionModel, segments[segmentIndex]));

    }

    private void clearSegmentArea(HieroglyphRecognitionModel model, MarvinSegment marvinSegment) {
        if (!checkForAvailableArea(marvinSegment))
            return;

        int[][] resizingVector = imageUtility.resizeVector(model, marvinSegment);

        if (!checkForEmptyArea(resizingVector))
            return;

        int[][] vector = model.getVector();
        Point pointStart = new Point(marvinSegment.x1, marvinSegment.y1);
        Point pointEnd = new Point(marvinSegment.x2, marvinSegment.y2);
        removeArea(vector, pointStart, pointEnd);
        HieroglyphRecognitionModel.builder(model)
                .vector(vector);
    }

    private void removeArea(int[][] vector, Point start, Point end) {
        int xStart = start.x;
        int xEnd = xStart + end.x + 1;
        int yStart = start.y;
        int yEnd = yStart + end.y + 1;
        if (vector.length < yEnd || vector[0].length < xEnd)
            return;
        IntStream.range(yStart, yEnd).forEach(height ->
                IntStream.range(xStart, xEnd).forEach(width -> vector[height][width] = 0));
    }

    private void formResult(MarvinSegment marvinSegment, MarvinSegment marvinSegmentInnerElement, HieroglyphRecognitionModel hieroglyphRecognitionModel, MarvinImage marvinImage) {
        int[][] resizingVector = imageUtility.resizeVector(hieroglyphRecognitionModel, marvinSegment);

        if (marvinSegmentInnerElement != null) {
            int[][] resizingVectorForInnerElement = imageUtility.resizeVector(hieroglyphRecognitionModel, marvinSegmentInnerElement);

            if (isIntersect(marvinSegment, marvinSegmentInnerElement, resizingVector, resizingVectorForInnerElement)) {
                Point start = new Point(Math.abs(marvinSegmentInnerElement.x1 - marvinSegment.x1),
                        Math.abs(marvinSegmentInnerElement.y1 - marvinSegment.y1));
                Point end = new Point(Math.abs(marvinSegmentInnerElement.x2 - marvinSegmentInnerElement.x1),
                        Math.abs(marvinSegmentInnerElement.y2 - marvinSegmentInnerElement.y1));
                removeArea(resizingVector, start, end);
            }
        }
        formResult(resizingVector, marvinSegment, marvinImage);
    }


    private boolean isIntersect(MarvinSegment marvinSegment, MarvinSegment marvinSegmentSecond, int[][] resizingVector, int[][] resingVectorSecond) {
        if (!checkForAvailableArea(marvinSegment) || !checkForAvailableArea(marvinSegmentSecond))
            return false;

        if (!checkForEmptyArea(resizingVector) || !checkForEmptyArea(resingVectorSecond))
            return false;

        Point pointAStart = new Point(marvinSegment.x1, marvinSegment.y1);
        Point pointAEnd = new Point(marvinSegment.x2, marvinSegment.y2);
        Point pointBStart = new Point(marvinSegmentSecond.x1, marvinSegmentSecond.y1);
        Point pointBEnd = new Point(marvinSegmentSecond.x2, marvinSegmentSecond.y2);
        return ((pointBStart.x <= pointAStart.x && pointAStart.x <= pointBEnd.x) || (pointAStart.x <= pointBStart.x && pointBStart.x <= pointAEnd.x))
                &&
                ((pointBStart.y <= pointAStart.y && pointAStart.y <= pointBEnd.y) || (pointAStart.y <= pointBStart.y && pointBStart.y <= pointAEnd.y));
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

    private boolean checkForAvailableArea(MarvinSegment marvinSegment) {
        return marvinSegment.area > THRESHOLD_FOR_AREA && marvinSegment.height > THRESHOLD_FOR_Y && marvinSegment.width > THRESHOLD_FOR_X;
    }

    private void formResult(int[][] resizingVector, MarvinSegment segmentResult, MarvinImage loadImage) {
        if (!checkForAvailableArea(segmentResult) || !checkForEmptyArea(resizingVector))
            return;

        HieroglyphRecognitionModel hieroglyphResizingRecognitionModel = recognitionModelMapUtility.mapToModel(resizingVector);
        hieroglyphRecognitionModels.add(hieroglyphResizingRecognitionModel);
        loadImage.drawRect(segmentResult.x1, segmentResult.y1, segmentResult.width, segmentResult.height, COLOR_SEGMENTS);
    }

    private boolean checkForEmptyArea(int[][] vector) {
        int height = vector.length;
        int[] resultList = new int[height];
        int thresholdHeight = Math.toIntExact(Math.round(height * 0.5));
        IntStream.range(0, height).forEach(heightIndex ->
                resultList[heightIndex] = Arrays.stream(vector[heightIndex]).max().getAsInt());

        long cntEmptyElement = Arrays.stream(resultList).filter(element -> element == 0).count();
        return cntEmptyElement < thresholdHeight;
    }

    private void saveResults(List<HieroglyphRecognitionModel> hieroglyphRecognitionModels) {
        hieroglyphRecognitionModels.forEach(hieroglyphRecognitionModel -> {
            try {
                fileUtility.createImage(hieroglyphRecognitionModel.getBufferedImage(), hieroglyphRecognitionModel.getPath());
            } catch (IOException e) {
                log.warn("File already defined. Override existing.");
            }
        });
    }
}
