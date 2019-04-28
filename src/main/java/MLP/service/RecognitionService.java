package MLP.service;

import MLP.exception.RecognitionException;
import MLP.model.HieroglyphRecognitionModel;
import MLP.model.Translation;
import MLP.service.file_manager.FileService;
import MLP.service.filtering.FilterService;
import MLP.service.image_manager.ImageService;
import MLP.service.resizing.ResizeService;
import MLP.service.rest_client.RestClient;
import MLP.service.segmentation.SegmentationService;
import MLP.service.translation.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * author: ElinaValieva on 07.04.2019
 * Main service for recognition
 */
@Service
public class RecognitionService {

    private final FilterService filterService;
    private final SegmentationService segmentationService;
    private final FileService fileService;
    private final RestClient restClient;
    private final TranslationService translationService;
    private final ResizeService resizeService;


    @Autowired
    public RecognitionService(FilterService filterService,
                              SegmentationService segmentationService,
                              FileService fileService,
                              RestClient restClient,
                              TranslationService translationService,
                              ImageService imageService,
                              ResizeService resizeService) {
        this.filterService = filterService;
        this.segmentationService = segmentationService;
        this.fileService = fileService;
        this.restClient = restClient;
        this.translationService = translationService;
        this.resizeService = resizeService;
    }

    public List<Translation> recognize(MultipartFile multipartFile) throws IOException, RecognitionException {
        fileService.deleteAll();
        String imagePath = fileService.createImage(multipartFile);
        filterService.filter(imagePath);
        List<HieroglyphRecognitionModel> segmentedHieroglyphs = segmentationService.segment(imagePath);
        resizeService.resizeHieroglyphs(segmentedHieroglyphs);
        List<Integer> resultFromNN = restClient.sendSegments(segmentedHieroglyphs.stream()
                .map(HieroglyphRecognitionModel::getPath)
                .collect(Collectors.toList()));
        
        return translationService.translate(resultFromNN);
    }
}
