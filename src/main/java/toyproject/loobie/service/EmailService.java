package toyproject.loobie.service;

import toyproject.loobie.web.dto.EmailMessageDto;

public interface EmailService {

    void sendEmail(EmailMessageDto emailMessage);
}
