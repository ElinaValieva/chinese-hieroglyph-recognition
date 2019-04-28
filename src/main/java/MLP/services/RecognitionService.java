package MLP.services;

import MLP.exception.RecognitionException;
import MLP.model.HieroglyphRecognitionModel;
import MLP.model.Translation;
import MLP.services.file_service.FileService;
import MLP.services.image_service.ImageService;
import MLP.services.preprocessing.FilterService;
import MLP.services.rest_client.RestClient;
import MLP.services.segmentation.SegmentationService;
import MLP.services.translation.TranslationService;
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
    private final ImageService imageService;
    private final RestClient restClient;
    private final TranslationService translationService;

    @Autowired
    public RecognitionService(FilterService filterService,
                              SegmentationService segmentationService,
                              FileService fileService,
                              RestClient restClient,
                              TranslationService translationService,
                              ImageService imageService) {
        this.filterService = filterService;
        this.segmentationService = segmentationService;
        this.fileService = fileService;
        this.restClient = restClient;
        this.translationService = translationService;
        this.imageService = imageService;
    }

    public List<Translation> recognize(MultipartFile multipartFile) throws IOException, RecognitionException {
        String imagePath = fileService.createImage(multipartFile);
        filterService.filter(imagePath);
        List<HieroglyphRecognitionModel> segmentedHieroglyphs = segmentationService.segment(imagePath);
        imageService.resizeHieroglyphs(segmentedHieroglyphs);
        List<Integer> resultFromNN = restClient.sendSegments(segmentedHieroglyphs.stream()
                .map(HieroglyphRecognitionModel::getPath)
                .collect(Collectors.toList()));
        return translationService.translate(resultFromNN);
    }
}
