package MLP.service.segmentation;


import MLP.exception.ErrorCode;
import MLP.exception.RecognitionException;
import MLP.model.HieroglyphRecognitionModel;
import MLP.service.file_manager.FileService;
import MLP.service.hieroglyph_mapper.HieroglyphMapperService;
import MLP.service.image_manager.ImageService;
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
import java.util.List;
import java.util.stream.IntStream;

import static MLP.service.segmentation.common.SegmentationConstants.*;
import static MLP.utility.IntersectionUtil.*;
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

    private final FileService fileService;
    private final ImageService imageService;
    private List<HieroglyphRecognitionModel> hieroglyphRecognitionModels;
    private final HieroglyphMapperService hieroglyphMapperService;

    @Autowired
    public SegmentationService(FileService fileService,
                               ImageService imageService, HieroglyphMapperService hieroglyphMapperService) {
        this.fileService = fileService;
        this.imageService = imageService;
        this.hieroglyphMapperService = hieroglyphMapperService;
    }

    public List<HieroglyphRecognitionModel> segment(String imagePath) throws RecognitionException, IOException {
        log.debug("Start segmenting process for image: {}", imagePath);
        HieroglyphRecognitionModel hieroglyphRecognitionModel = hieroglyphMapperService.mapToModel(imagePath);
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

        MarvinImageIO.saveImage(loadImage, fileService.getPathDirectory(SEGMENTATION_RESULT_FILE_NAME));
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
        int[][] resizingVector = imageService.resizeVector(hieroglyphRecognitionModel, marvinSegment);

        if (marvinSegmentInnerElement != null) {
            int[][] resizingVectorForInnerElement = imageService.resizeVector(hieroglyphRecognitionModel, marvinSegmentInnerElement);

            if (isInnerIntersect(marvinSegment, marvinSegmentInnerElement, resizingVector, resizingVectorForInnerElement)) {
                Point start = new Point(Math.abs(marvinSegmentInnerElement.x1 - marvinSegment.x1),
                        Math.abs(marvinSegmentInnerElement.y1 - marvinSegment.y1));
                Point end = new Point(Math.abs(marvinSegmentInnerElement.x2 - marvinSegmentInnerElement.x1),
                        Math.abs(marvinSegmentInnerElement.y2 - marvinSegmentInnerElement.y1));
                removeArea(resizingVector, start, end);
            }
        }
        formResult(resizingVector, marvinSegment, marvinImage);
    }


    private boolean isInnerIntersect(MarvinSegment marvinSegment, MarvinSegment marvinSegmentSecond, int[][] resizingVector, int[][] resizingVectorSecond) {
        if (isAvailableArea(marvinSegment) || isAvailableArea(marvinSegmentSecond))
            return false;

        if (isEmptyArea(resizingVector) || isEmptyArea(resizingVectorSecond))
            return false;

        return innerIntersection(marvinSegment, marvinSegmentSecond) ||
                innerIntersection(marvinSegmentSecond, marvinSegment);
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

    private void formResult(int[][] resizingVector, MarvinSegment segmentResult, MarvinImage loadImage) {
        if (isAvailableArea(segmentResult) || isEmptyArea(resizingVector))
            return;

        HieroglyphRecognitionModel hieroglyphResizingRecognitionModel = hieroglyphMapperService.mapToModel(resizingVector);
        hieroglyphRecognitionModels.add(hieroglyphResizingRecognitionModel);
        loadImage.drawRect(segmentResult.x1, segmentResult.y1, segmentResult.width, segmentResult.height, COLOR_SEGMENTS);
    }
}
