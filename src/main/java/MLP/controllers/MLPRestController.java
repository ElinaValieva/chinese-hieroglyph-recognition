package MLP.controllers;

import MLP.services.TransformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author: ElinaValieva on 15.12.2018
 */
@RestController
public class MLPRestController {

    @Autowired
    private TransformationService transformationService;

    @GetMapping("/convert")
    public void segment(){
        transformationService.transform();
    }
}
