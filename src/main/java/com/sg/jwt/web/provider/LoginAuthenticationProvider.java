package com.sg.jwt.web.provider;

import com.sg.jwt.exception.LoginException;
import com.sg.jwt.web.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class LoginAuthenticationProvider implements AuthenticationProvider {

  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  private final LoginService loginService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    final var userId = authentication.getPrincipal().toString();
    final var password = authentication.getCredentials().toString();

    final var user = this.loginService.getUser(userId);
    if (!this.passwordEncoder.matches(password, user.getPassword())) {
      throw new LoginException();
    }

    final var result = UsernamePasswordAuthenticationToken.authenticated(userId, "", null);
    result.setDetails(this.loginService.getJwtInfo(userId));

    return result;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
