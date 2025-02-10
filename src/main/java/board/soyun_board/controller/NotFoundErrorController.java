package board.soyun_board.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NotFoundErrorController implements ErrorController {

    @RequestMapping("/error")
    public String error() {
        return "forward:/index.html"; // React index.html을 반환
    }
}
