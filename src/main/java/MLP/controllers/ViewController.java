package MLP.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * author: ElinaValieva on 15.12.2018
 */
@Controller
public class ViewController {

    @RequestMapping("/")
    public String getStartPage() {
        return "start";
    }
}
