package dev.webserver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "${api.base.path}")
class WebController {

    @GetMapping
    public String hello() {
        return "Hello world!";
    }

}
