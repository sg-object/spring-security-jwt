package com.sg.jwt.web.filter;

import com.sg.jwt.web.service.LoginService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import java.io.IOException;

public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  private final LoginService loginService;

  public LoginAuthenticationFilter(final RequestMatcher requestMatcher, final LoginService loginService) {
    super(requestMatcher);
    this.loginService = loginService;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    final var user = this.loginService.getLoginUser(request);
    return getAuthenticationManager().authenticate(UsernamePasswordAuthenticationToken.unauthenticated(user.getId(), user.getPassword()));
  }
}
