package toyproject.loobie.config.auth.dto;

import lombok.Getter;
import toyproject.loobie.domain.user.User;

import java.io.Serializable;

/**
 * 직렬화 기능을 가진 User클래스
 */
@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String profileImage;

    public SessionUser(User user){
        this.name = user.getName();
        this.email = user.getEmail();
        this.profileImage = user.getProfileImage();
    }
}
