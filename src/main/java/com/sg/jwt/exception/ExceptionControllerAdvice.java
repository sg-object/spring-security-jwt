package com.sg.jwt.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(RuntimeException.class)
  public String handleRuntimeException(final Exception e) {
    log.error(e.getMessage(), e);
    return e.getMessage();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(LoginException.class)
  public String handleLoginException(final Exception e) {
    log.error(e.getMessage(), e);
    return e.getMessage();
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(SignatureException.class)
  public String handleSignatureException(final Exception e) {
    log.error(e.getMessage(), e);
    return e.getMessage();
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(RefreshTokenException.class)
  public String handleRefreshTokenException(final Exception e) {
    log.error(e.getMessage(), e);
    return e.getMessage();
  }

  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler(ExpiredJwtException.class)
  public String handleExpiredJwtException(final Exception e) {
    log.error(e.getMessage(), e);
    return e.getMessage();
  }
}
