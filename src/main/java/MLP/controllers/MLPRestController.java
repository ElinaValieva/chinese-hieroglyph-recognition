package MLP.controllers;

import MLP.services.TransformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static MLP.configs.URL.FILE;
import static MLP.configs.URL.URL_UPLOAD;

/**
 * author: ElinaValieva on 15.12.2018
 */
@RestController
public class MLPRestController {

    @Autowired
    private TransformationService transformationService;


    @PostMapping(value = URL_UPLOAD, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus uploadFileMulti(@RequestParam(FILE) MultipartFile multipartFile) throws IOException {
        transformationService.transform(multipartFile);
        return HttpStatus.OK;
    }
}
