package MLP.services;

import MLP.exception.RecognitionException;
import MLP.services.preprocessing.FilterService;
import MLP.services.segmentation.SegmentationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * author: ElinaValieva on 07.04.2019
 * Main service for recognition
 */
@Service
public class HRService {

    private final FilterService filterService;
    private final SegmentationService segmentationService;

    @Autowired
    public HRService(FilterService filterService, SegmentationService segmentationService) {
        this.filterService = filterService;
        this.segmentationService = segmentationService;
    }

    public void recognize(String imagePath) throws IOException, RecognitionException {
        filterService.filter(imagePath);
        segmentationService.segment(imagePath);
    }
}
