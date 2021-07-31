package toyproject.loobie.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import toyproject.loobie.web.dto.EmailMessageDto;

@Profile("local")
@Component @Slf4j
public class ConsoleEmailService implements EmailService{
    @Override
    public void sendEmail(EmailMessageDto emailMessage) {
        log.info("sent email: {}", emailMessage.getMessage());
    }
}
