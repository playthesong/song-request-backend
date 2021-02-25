package com.requestrealpiano.songrequest.config.security;

import com.requestrealpiano.songrequest.config.security.oauth.CustomOAuth2UserService;
import com.requestrealpiano.songrequest.domain.account.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
            .disable();

        http.authorizeRequests()
            .antMatchers("/api/**").hasAnyRole(Role.MEMBER.getKey(), Role.ADMIN.getKey())
            .anyRequest().authenticated();

//        http.sessionManagement()
//            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService);
    }
}
