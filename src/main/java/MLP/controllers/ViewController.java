package MLP.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static MLP.configs.URL.URL_VIEW;
import static MLP.configs.URL.VIEW;

/**
 * author: ElinaValieva on 15.12.2018
 */
@Controller
public class ViewController {

    @RequestMapping(URL_VIEW)
    public String getStartPage() {
        return VIEW;
    }
}
