package live.playthesong.songrequest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/auth")
public class LoginController {

    @GetMapping("/google")
    public String googleRedirect() {
        return "login";
    }
}
