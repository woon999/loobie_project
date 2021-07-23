package toyproject.loobie.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import toyproject.loobie.domain.user.User;
import toyproject.loobie.domain.user.UserRepository;
import toyproject.loobie.web.dto.UserSaveRequestDto;

import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service @Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;

    // 유저 저장
    @Transactional
    public Long save(UserSaveRequestDto requestDto) {
        return userRepository.save(requestDto.toEntity()).getId();
    }
    @Transactional(readOnly = true)
    public User findOne(Long id) {
        return userRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()){
            return null;
        }else{
            return user.orElseThrow();
        }
    }

    @Transactional
    public void processNewSubscriber(String name, String email) {
        // 구독 유저 등록
        User newUser = saveSubscribeUser(name, email);
        newUser.generateEmailToken();
        // 구독 확인 메일 전송
        sendSubscribeConfirmEmail(newUser);
    }

    // 구독 유저 등록
    private User saveSubscribeUser(@RequestParam("name") String name, @RequestParam("email") String email) {
        UserSaveRequestDto requestDto = UserSaveRequestDto.builder()
                .name(name)
                .email(email)
                .build();
        Long newId = save(requestDto);
        log.info("###user 생성 " + newId);

        //TODO : 인증 이메일 전송
        return findOne(newId);
    }

    // 구독 확인 메일 전송
    private void sendSubscribeConfirmEmail(User newUser) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(newUser.getEmail());
        mailMessage.setSubject("닷 뉴스, 이메일 인증");
        mailMessage.setText("/check-email-token?token=" + newUser.getEmailCheckToken() +"&email=" + newUser.getEmail());
        javaMailSender.send(mailMessage);
    }


    @Transactional
    public void emailVerified(String email) {
        User user = userRepository.findByEmail(email)
                .map(entity -> entity.emailVerified(true))
                .orElseThrow();
        userRepository.save(user);
    }

}
