package toyproject.loobie.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.loobie.domain.user.Role;
import toyproject.loobie.domain.user.User;

@Getter
@NoArgsConstructor
public class UserSaveRequestDto {

    private String name;
    private String email;

    @Builder
    public UserSaveRequestDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User toEntity(){
        return User.builder()
                .name(name)
                .email(email)
                .role(Role.USER)
                .build();
    }
}
