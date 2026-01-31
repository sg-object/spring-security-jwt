package com.sg.jwt.web.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import java.io.IOException;

@RequiredArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {

  private final HandlerExceptionResolver handlerExceptionResolver;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    this.handlerExceptionResolver.resolveException(request, response, null, exception);
  }
}
