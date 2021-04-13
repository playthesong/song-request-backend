package live.playthesong.songrequest.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.playthesong.songrequest.domain.account.AccountRepository;
import live.playthesong.songrequest.domain.account.Role;
import live.playthesong.songrequest.security.filter.JwtAuthorizationFilter;
import live.playthesong.songrequest.security.jwt.JwtTokenProvider;
import live.playthesong.songrequest.security.oauth.CustomAccessDeniedHandler;
import live.playthesong.songrequest.security.oauth.CustomAuthenticationEntryPoint;
import live.playthesong.songrequest.security.oauth.CustomAuthenticationSuccessHandler;
import live.playthesong.songrequest.security.oauth.CustomOAuth2UserService;
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
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final AccountRepository accountRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    private final String ADMIN = Role.ADMIN.getKey();
    private final String MEMBER = Role.MEMBER.getKey();
    private final String GUEST = Role.GUEST.getKey();

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());

        web.ignoring().antMatchers(HttpMethod.GET, "/api/accounts/auth")
                      .antMatchers(HttpMethod.GET, "/api/accounts/auth/validation")
                      .antMatchers(HttpMethod.GET, "/api/auth/google")
                      .antMatchers(HttpMethod.GET, "/api/letters/**")
                      .antMatchers(HttpMethod.GET, "/api/songs/ranking")
        ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors(withDefaults());

        http.csrf().disable()
            .httpBasic().disable();

        http.authorizeRequests()
            .antMatchers(HttpMethod.GET, "/api/admin/**").hasRole(ADMIN)
            .antMatchers(HttpMethod.POST, "/api/admin/**").hasRole(ADMIN)
            .antMatchers(HttpMethod.POST, "/api/letters/**").hasAnyRole(MEMBER, ADMIN)
            .antMatchers(HttpMethod.PUT, "/api/letters/{id}/status").hasRole(ADMIN)
            .antMatchers(HttpMethod.PUT, "/api/letters/**").hasAnyRole(MEMBER, ADMIN)
            .antMatchers(HttpMethod.DELETE, "/api/letters/yesterday").hasRole(ADMIN)
            .antMatchers(HttpMethod.DELETE, "/api/letters/{id}").hasAnyRole(MEMBER, ADMIN)
            .antMatchers(HttpMethod.GET, "/api/songs/**").hasAnyRole(MEMBER, ADMIN)
            .antMatchers(HttpMethod.GET, "/api/accounts/detail").hasAnyRole(GUEST, MEMBER, ADMIN)
            .antMatchers(HttpMethod.DELETE, "/api/accounts").hasAnyRole(GUEST, MEMBER, ADMIN)
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
            .authenticationEntryPoint(customAuthenticationEntryPoint)
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
