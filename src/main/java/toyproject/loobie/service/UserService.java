package toyproject.loobie.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toyproject.loobie.domain.user.User;
import toyproject.loobie.domain.user.UserRepository;
import toyproject.loobie.web.dto.UserSaveRequestDto;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 유저 저장
    @Transactional
    public Long save(UserSaveRequestDto requestDto) {
        return userRepository.save(requestDto.toEntity()).getId();
    }

    public boolean findByEmail(String email) {
        if(userRepository.findByEmail(email).isEmpty()){
            return false;
        }else{
            return true;
        }
    }
}
