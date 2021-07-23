package toyproject.loobie.web;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import toyproject.loobie.domain.user.User;
import toyproject.loobie.service.UserService;
import toyproject.loobie.web.dto.UserSaveRequestDto;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    /**
     * 유저 구독하기
     */
    @PostMapping("/subscribe")
    public String clientSave(@RequestParam("name") String name,
                             @RequestParam("email") String email,
                             RedirectAttributes attributes){
        if(userService.findByEmail(email)!=null){
            attributes.addFlashAttribute("subMessage", "이미 구독이 완료된 이메일입니다.");
            return "redirect:/";
        }

        userService.processNewSubscriber(name, email);

        attributes.addFlashAttribute("subMessage", "해당 이메일로 인증 메일이 발송되었습니다.");
        return "redirect:/";
    }


    /**
     * email 토큰 발급
     */
    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model){
        User checkUser = userService.findByEmail(email);
        if(checkUser==null){
            // 유저가 존재하지 않습니다.
            model.addAttribute("error", "wrong.email");
            return "client/email-verified";
        }

        if(!checkUser.getEmailCheckToken().equals(token)){
            // 토큰이 일치하지 않습니다.
            model.addAttribute("error", "wrong.token");
            return "client/email-verified";
        }

        userService.emailVerified(email);
        model.addAttribute("name", checkUser.getName());
        return "client/email-verified";
    }

}
