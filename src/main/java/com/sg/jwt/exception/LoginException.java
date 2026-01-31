package com.sg.jwt.exception;

import org.springframework.security.core.AuthenticationException;

public class LoginException extends AuthenticationException {

  public LoginException() {
    super(null);
  }
}
