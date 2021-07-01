package toyproject.loobie.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import toyproject.loobie.config.auth.LoginUser;
import toyproject.loobie.config.auth.dto.SessionUser;

@Controller
@RequiredArgsConstructor
public class HomeController {


    @GetMapping("/")
    public String home(Model model, @LoginUser SessionUser user){
        if(user != null){
            model.addAttribute("userName", user.getName());
        }
        return "home"; //
    }
}
