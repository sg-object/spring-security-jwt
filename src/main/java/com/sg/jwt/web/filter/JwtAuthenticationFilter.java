package com.sg.jwt.web.filter;

import com.sg.jwt.token.service.JwtService;
import com.sg.jwt.web.model.UserInfo;
import com.sg.jwt.web.value.HeaderKey;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  private final HandlerExceptionResolver handlerExceptionResolver;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      final var accessToken = this.jwtService.getAuthorizationToken(request.getHeader(HeaderKey.AUTHORIZATION));
      final var claims = this.jwtService.verify(accessToken);
      final var user = new UserInfo(this.jwtService.getUserIdFromClaims(claims.getPayload()));
      SecurityContextHolder.getContext().setAuthentication(UsernamePasswordAuthenticationToken.authenticated(user, "", null));
      filterChain.doFilter(request, response);
    } catch (final Exception e) {
      this.handlerExceptionResolver.resolveException(request, response, null, e);
    }
  }
}
