package toyproject.loobie.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import toyproject.loobie.domain.user.User;
import toyproject.loobie.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @MockBean
    JavaMailSender javaMailSender;

    @Test
    public void 유저_구독하기() throws Exception{
        String email = "abc@naver.com";
        String name = "loosie";
        mockMvc.perform(post("/subscribe")
                .param("name", name)
                .param("email", email))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        User user = userService.findByEmail("abc@naver.com");
        assertThat(user.getName()).isEqualTo(name);
        then(javaMailSender).should().send(any(SimpleMailMessage.class));
    }

}