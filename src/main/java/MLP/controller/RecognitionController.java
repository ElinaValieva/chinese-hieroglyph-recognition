package MLP.controller;

import MLP.services.recognition.models.ImageSegmentation;
import MLP.services.recognition.RecognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static MLP.common.ApplicationConstants.FILE;
import static MLP.common.ApplicationConstants.URL_UPLOAD;

/**
 * author: ElinaValieva on 15.12.2018
 */
@RestController
public class RecognitionController {

    private final RecognitionService recognitionService;

    @Autowired
    public RecognitionController(RecognitionService recognitionService) {
        this.recognitionService = recognitionService;
    }

    @PostMapping(value = URL_UPLOAD, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadFileMulti(@RequestParam(FILE) MultipartFile multipartFile) throws IOException {
        List<ImageSegmentation> imageSegmentations = recognitionService.transform(multipartFile);
        return ResponseEntity.ok(imageSegmentations);
    }
}
