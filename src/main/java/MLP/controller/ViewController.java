package MLP.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static MLP.common.ApplicationConstants.URL_VIEW;
import static MLP.common.ApplicationConstants.VIEW;

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
