package live.playthesong.songrequest.testconfig.security.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class MockAuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        SecurityContextHolder.getContext()
                             .setAuthentication((Authentication) ((HttpServletRequest) request)
                             .getUserPrincipal());
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        SecurityContextHolder.clearContext();
    }
}
