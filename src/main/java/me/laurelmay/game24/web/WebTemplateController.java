package me.laurelmay.game24.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebTemplateController {

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/solve")
    public String createGame() {
        return "solve.html";
    }

    @GetMapping("/about")
    public String about() {
        return "about.html";
    }
}
