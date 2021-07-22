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
    private final JavaMailSender javaMailSender;

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

        UserSaveRequestDto requestDto = UserSaveRequestDto.builder()
                .name(name)
                .email(email)
                .build();
        Long newId = userService.save(requestDto);
//        System.out.println("###user 생성 " + id);

        //TODO : 인증 이메일 전송
        User newUser = userService.findOne(newId);
        newUser.generateEmailToken();
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(newUser.getEmail());
        mailMessage.setSubject("닷 뉴스, 이메일 인증");
        mailMessage.setText("/check-email-token?token=" + newUser.getEmailCheckToken() +"&email=" + newUser.getEmail());
        javaMailSender.send(mailMessage);

        attributes.addFlashAttribute("subMessage", "해당 이메일로 인증 메일이 발송되었습니다.");
        return "redirect:/";
    }

}
