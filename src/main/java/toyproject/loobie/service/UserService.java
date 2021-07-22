package toyproject.loobie.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toyproject.loobie.domain.news.News;
import toyproject.loobie.domain.user.User;
import toyproject.loobie.domain.user.UserRepository;
import toyproject.loobie.web.dto.UserSaveRequestDto;

import org.springframework.transaction.annotation.Transactional;

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
}
