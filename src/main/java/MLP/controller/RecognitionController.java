package MLP.controller;

import MLP.exception.RecognitionException;
import MLP.services.RecognitionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static MLP.common.Constants.FILE;
import static MLP.common.Constants.URL_UPLOAD;

/**
 * author: ElinaValieva on 15.12.2018
 */
@RestController
public class RecognitionController {

    private final RecognitionService recognitionService;

    public RecognitionController(RecognitionService recognitionService) {
        this.recognitionService = recognitionService;
    }

    @PostMapping(value = URL_UPLOAD, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus uploadFileMulti(@RequestParam(FILE) MultipartFile multipartFile) throws IOException, RecognitionException {
        recognitionService.recognize(multipartFile);
        return HttpStatus.OK;
    }
}
