package MLP.services;

import MLP.exception.RecognitionException;
import MLP.model.HieroglyphRecognitionModel;
import MLP.services.preprocessing.FilterService;
import MLP.services.segmentation.SegmentationService;
import MLP.utility.FileUtility;
import MLP.utility.ResizeUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private final FileUtility fileUtility;

    @Autowired
    public RecognitionService(FilterService filterService, SegmentationService segmentationService, ResizeUtility resizeUtility, FileUtility fileUtility) {
        this.filterService = filterService;
        this.segmentationService = segmentationService;
        this.resizeUtility = resizeUtility;
        this.fileUtility = fileUtility;
    }

    public List<HieroglyphRecognitionModel> recognize(MultipartFile multipartFile) throws IOException, RecognitionException {
        String imagePath = fileUtility.createImage(multipartFile);
        filterService.filter(imagePath);
        List<HieroglyphRecognitionModel> segmentedHieroglyphs = segmentationService.segment(imagePath);
        resizeUtility.resizeHieroglyphs(segmentedHieroglyphs);
        return segmentedHieroglyphs;
    }
}
