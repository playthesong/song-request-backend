package com.requestrealpiano.songrequest.testconfig.security;

import com.requestrealpiano.songrequest.domain.account.Role;
import com.requestrealpiano.songrequest.security.oauth.CustomAccessDeniedHandler;
import com.requestrealpiano.songrequest.security.oauth.CustomAuthenticationEntryPoint;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

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
                      .antMatchers(HttpMethod.GET, "/api/letters/**")
                      .antMatchers(HttpMethod.GET, "/api/songs");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .httpBasic().disable();

        http.authorizeRequests()
            .antMatchers("/api/letters").hasAnyRole(Role.MEMBER.getKey(), Role.ADMIN.getKey())
            .anyRequest().authenticated();

        http.addFilterBefore(characterEncodingFilter(), CsrfFilter.class);

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
