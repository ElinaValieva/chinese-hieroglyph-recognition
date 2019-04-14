package MLP.services;

import MLP.exception.RecognitionException;
import MLP.model.HieroglyphRecognitionModel;
import MLP.services.preprocessing.FilterService;
import MLP.services.segmentation.SegmentationService;
import MLP.utility.ResizeUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * author: ElinaValieva on 07.04.2019
 * Main service for recognition
 */
@Service
public class RecognitionService {

    private final FilterService filterService;
    private final SegmentationService segmentationService;
    private final ResizeUtility resizeUtility;

    @Autowired
    public RecognitionService(FilterService filterService, SegmentationService segmentationService, ResizeUtility resizeUtility) {
        this.filterService = filterService;
        this.segmentationService = segmentationService;
        this.resizeUtility = resizeUtility;
    }

    public List<HieroglyphRecognitionModel> recognize(String imagePath) throws IOException, RecognitionException {
        filterService.filter(imagePath);
        List<HieroglyphRecognitionModel> segmentedHieroglyphs = segmentationService.segment(imagePath);
        resizeUtility.resizeHieroglyphs(segmentedHieroglyphs);
        return segmentedHieroglyphs;
    }
}
