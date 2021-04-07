package com.requestrealpiano.songrequest.testconfig.security;

import com.requestrealpiano.songrequest.security.oauth.CustomAccessDeniedHandler;
import com.requestrealpiano.songrequest.security.oauth.CustomAuthenticationEntryPoint;
import com.requestrealpiano.songrequest.testconfig.security.filter.MockAuthenticationFilter;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import static com.requestrealpiano.songrequest.domain.account.Role.*;

@TestConfiguration
@Import({CustomAuthenticationEntryPoint.class, CustomAccessDeniedHandler.class})
public class SecurityTestConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    public SecurityTestConfig(CustomAuthenticationEntryPoint customAuthenticationEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());

        web.ignoring().antMatchers(HttpMethod.GET, "/api/accounts/auth")
                      .antMatchers(HttpMethod.GET, "/api/accounts/auth/validation")
                      .antMatchers(HttpMethod.GET, "/api/letters/**")
        ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .httpBasic().disable();

        http.authorizeRequests()
            .antMatchers(HttpMethod.POST, "/api/admin/**").hasRole(ADMIN.getKey())
            .antMatchers(HttpMethod.POST, "/api/letters/**").hasAnyRole(MEMBER.getKey(), ADMIN.getKey())
            .antMatchers(HttpMethod.PUT, "/api/letters/{id}/status").hasRole(ADMIN.getKey())
            .antMatchers(HttpMethod.PUT, "/api/letters/**").hasAnyRole(MEMBER.getKey(), ADMIN.getKey())
            .antMatchers(HttpMethod.DELETE, "/api/letters/yesterday").hasRole(ADMIN.getKey())
            .antMatchers(HttpMethod.DELETE, "/api/letters/{id}").hasAnyRole(MEMBER.getKey(), ADMIN.getKey())
            .antMatchers(HttpMethod.GET, "/api/songs/**").hasAnyRole(MEMBER.getKey(), ADMIN.getKey())
            .antMatchers(HttpMethod.GET, "/api/accounts/detail").hasAnyRole(MEMBER.getKey(), ADMIN.getKey())
            .antMatchers(HttpMethod.DELETE, "/api/accounts").hasAnyRole(GUEST.getKey(), MEMBER.getKey(), ADMIN.getKey())
            .anyRequest().authenticated();

        http.addFilterBefore(characterEncodingFilter(), CsrfFilter.class)
            .addFilterBefore(new MockAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

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
}
