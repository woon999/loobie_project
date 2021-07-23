package toyproject.loobie.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable() // h2-console 화면 사용하기 위해 해당 옵션 disable
                .and()
                    .authorizeRequests() // URL별 권한 관리
                    .antMatchers("/", "/news/*", "/search", "/subscribe", "/check-email-token",
                            "/css/**","/images/**","/js/**","/h2-console/**").permitAll()
//                    .antMatchers("/api/v1/**").hasRole(Role.ADMIN.name())
                // TODO : /admin 접근권한 설정
                    .anyRequest().authenticated()
                .and()
                    .logout()
                        .logoutSuccessUrl("/")
                .and()
                    .oauth2Login()
                        .userInfoEndpoint()
                            .userService(customOAuth2UserService);

//        super.configure(http);

    }
}
