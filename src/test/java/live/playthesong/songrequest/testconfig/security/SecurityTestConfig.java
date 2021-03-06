package live.playthesong.songrequest.testconfig.security;

import live.playthesong.songrequest.domain.account.Role;
import live.playthesong.songrequest.security.oauth.CustomAccessDeniedHandler;
import live.playthesong.songrequest.security.oauth.CustomAuthenticationEntryPoint;
import live.playthesong.songrequest.testconfig.security.filter.MockAuthenticationFilter;
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

@TestConfiguration
@Import({CustomAuthenticationEntryPoint.class, CustomAccessDeniedHandler.class})
public class SecurityTestConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private final String ADMIN = Role.ADMIN.getKey();
    private final String MEMBER = Role.MEMBER.getKey();
    private final String GUEST = Role.GUEST.getKey();

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
                      .antMatchers(HttpMethod.GET, "/api/songs/ranking")
        ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
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
