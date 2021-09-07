package toyproject.loobie.repository.user;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import toyproject.loobie.domain.user.Role;
import toyproject.loobie.domain.user.User;
import toyproject.loobie.domain.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

@RunWith(SpringRunner.class)
@SpringBootTest @Transactional
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @After
    public void cleanup(){

    }

    @Test
    public void 회원저장_이메일불러오기(){
        //given
        String name = "test";
        String email = "test@naver.com";

        User user = User.builder()
                .name(name)
                .email(email)
                .role(Role.USER)
                .build();

        userRepository.save(user);

        //when
        Optional<User> testUser = userRepository.findByEmail(email);

        if(testUser !=null){
            //then
            User getUser = testUser.get();
            assertThat(getUser.getName()).isEqualTo(name);
            assertThat(getUser.getEmail()).isEqualTo(email);
        }else {
            assertThatNullPointerException();
        }
    }

    @Test
    public void BaseTimeEntity_등록() throws Exception{
        //given
        String name = "test";
        String email = "test@naver.com";
        LocalDateTime now = LocalDateTime.of(2021,06,30,0,0,0);
        userRepository.save(User.builder()
                .name(name)
                .email(email)
                .role(Role.USER)
                .build());

        //when
        List<User> userList = userRepository.findAll();

        //then
        User users = userList.get(0);

        System.out.println(">>>>>> createdDate="+users.getCreatedDate()+", modifiedDate="+users.getModifiedDate());

        assertThat(users.getCreatedDate()).isAfter(now);
        assertThat(users.getModifiedDate()).isAfter(now);
    }



}