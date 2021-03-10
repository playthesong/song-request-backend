package com.requestrealpiano.songrequest.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.requestrealpiano.songrequest.security.filter.JwtAuthorizationFilter;
import com.requestrealpiano.songrequest.security.jwt.JwtTokenProvider;
import com.requestrealpiano.songrequest.security.oauth.CustomAccessDeniedHandler;
import com.requestrealpiano.songrequest.security.oauth.CustomAuthenticationSuccessHandler;
import com.requestrealpiano.songrequest.security.oauth.CustomOAuth2UserService;
import com.requestrealpiano.songrequest.domain.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final AccountRepository accountRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());

        web.ignoring().antMatchers(HttpMethod.GET, "/api/accounts/auth")
//                      .antMatchers(HttpMethod.GET, "/api/letters/**")
                      .antMatchers(HttpMethod.GET, "/api/songs")
        ;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors(withDefaults());

        http.csrf().disable()
            .httpBasic().disable();

        http.authorizeRequests()
            .antMatchers("/**").permitAll()
//            .antMatchers("/", "/api/account/token").permitAll()
//            .antMatchers("/api/letters").hasAnyRole(Role.MEMBER.getValue(), Role.ADMIN.getValue())
            .anyRequest().authenticated();

        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(characterEncodingFilter(), CsrfFilter.class)
            .addFilterBefore(JwtAuthorizationFilter.of(accountRepository, jwtTokenProvider, objectMapper), UsernamePasswordAuthenticationFilter.class);

        http.oauth2Login()
            .userInfoEndpoint()
            .userService(customOAuth2UserService)
                .and()
            .successHandler(customAuthenticationSuccessHandler);

        http.exceptionHandling()
            .accessDeniedHandler(customAccessDeniedHandler);
    }

    public CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);
        return encodingFilter;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setExposedHeaders(Collections.singletonList("Authorization"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
