package toyproject.loobie.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.loobie.domain.BaseTimeEntity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter @NoArgsConstructor
public class User extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String emailCheckToken;

    private boolean emailVerified;

    @Builder
    public User(String name, String email, String profileImage, Role role) {
        this.name = name;
        this.email = email;
        this.profileImage = profileImage;
        this.role = role;
    }

    public User update(String name, String profileImage){
        this.name = name;
        this.profileImage = profileImage;
        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }

    public void generateEmailToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
    }
}
