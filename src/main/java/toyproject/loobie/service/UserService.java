package toyproject.loobie.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import toyproject.loobie.config.AppProperties;
import toyproject.loobie.domain.news.News;
import toyproject.loobie.domain.user.User;
import toyproject.loobie.domain.user.UserRepository;
import toyproject.loobie.web.dto.EmailMessageDto;
import toyproject.loobie.web.dto.UserSaveRequestDto;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service @Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final TemplateEngine templateEngine;
    private final AppProperties appProperties;

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
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return null;
        } else {
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

    @Transactional
    public void emailVerified(String email) {
        User user = userRepository.findByEmail(email)
                .map(entity -> entity.emailVerified(true))
                .orElseThrow();
        userRepository.save(user);
    }

    // 구독 유저 등록
    private User saveSubscribeUser(@RequestParam("name") String name, @RequestParam("email") String email) {
        UserSaveRequestDto requestDto = UserSaveRequestDto.builder()
                .name(name)
                .email(email)
                .build();
        Long newId = save(requestDto);
        log.info("#user 등록 :  " + newId);

        return findOne(newId);
    }

    // 구독 확인 메일 전송
    private void sendSubscribeConfirmEmail(User newUser) {
        Context context = new Context();
        context.setVariable("link", "/check-email-token?token=" + newUser.getEmailCheckToken() +"&email=" + newUser.getEmail());
        context.setVariable("name", newUser.getName());
        context.setVariable("linkName", "이메일 구독 확인하기");
        context.setVariable("message", "해당 링크를 통해서 닷 뉴스 구독을 확인해주세요.");
        context.setVariable("host", appProperties.getHost());

        String message = templateEngine.process("mail/simple-link", context);

        EmailMessageDto emailMessageDto =  EmailMessageDto.builder()
                                            .to(newUser.getEmail())
                                            .subject("닷 뉴스, 이메일 구독 안내")
                                            .message(message)
                                            .build();

        emailService.sendEmail(emailMessageDto);
    }


    // 뉴스 메일 전송
    public void sendNewsEmail(String email, News news) {
        Context context = new Context();
        context.setVariable("articles", news.getArticles());
        context.setVariable("economics", news.getEconomics());
        context.setVariable("host", appProperties.getHost());
        String message = templateEngine.process("mail/email-news-form", context);

        EmailMessageDto emailMessageDto =  EmailMessageDto.builder()
                .to(email)
                .subject(news.getDate()+ ". 오늘의 헤드라인 뉴스")
                .message(message)
                .build();

        emailService.sendEmail(emailMessageDto);
    }

}
