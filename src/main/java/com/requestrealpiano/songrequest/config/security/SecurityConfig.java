package com.requestrealpiano.songrequest.config.security;

import com.requestrealpiano.songrequest.config.security.jwt.JwtProperties;
import com.requestrealpiano.songrequest.config.security.oauth.CustomAuthenticationSuccessHandler;
import com.requestrealpiano.songrequest.config.security.oauth.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final JwtProperties jwtProperties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors(withDefaults());

        http.csrf().disable()
            .httpBasic().disable();

        http.authorizeRequests()
            .antMatchers("/**").permitAll()
//            .antMatchers("/", "/login/oauth2/code/**").permitAll()
//            .antMatchers("/api/**").hasAnyRole(Role.MEMBER.getValue(), Role.ADMIN.getValue())
            .anyRequest().authenticated();

        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.oauth2Login()
            .userInfoEndpoint()
            .userService(customOAuth2UserService)
                .and()
            .successHandler(customAuthenticationSuccessHandler)
            .defaultSuccessUrl(jwtProperties.getTokenUrl(), true);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
