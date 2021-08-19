package toyproject.loobie.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import toyproject.loobie.web.dto.EmailMessageDto;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Profile({"dev","real"})
@Component
@RequiredArgsConstructor
public class HtmlEmailService implements EmailService{

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(EmailMessageDto emailMessagedto) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessagedto.getTo());
            mimeMessageHelper.setSubject(emailMessagedto.getSubject());
            mimeMessageHelper.setText(emailMessagedto.getMessage(), true);
            javaMailSender.send(mimeMessage);
            log.info("sent email: {}", emailMessagedto.getSubject());
        } catch (MessagingException e) {
            log.error("failed to send email", e);
        }

    }
}
