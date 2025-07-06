package dgu.newsee.global;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public String healthCheck() {
        return "I'm healthy!";
    }

}