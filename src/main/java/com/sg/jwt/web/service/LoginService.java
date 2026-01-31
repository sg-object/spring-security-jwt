package com.sg.jwt.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sg.jwt.exception.LoginException;
import com.sg.jwt.token.model.JwtInfo;
import com.sg.jwt.token.service.JwtService;
import com.sg.jwt.user.model.User;
import com.sg.jwt.user.service.UserService;
import com.sg.jwt.web.model.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;
import java.io.IOException;

@RequiredArgsConstructor
@Service
public class LoginService {

  private final ObjectMapper objectMapper;

  private final Validator validator;

  private final JwtService jwtService;

  private final UserService userService;

  public LoginUser getLoginUser(final HttpServletRequest request) throws IOException {
    final var user = this.objectMapper.readValue(request.getReader(), LoginUser.class);
    final var errors = this.validator.validateObject(user);
    if (errors.hasErrors()) {
      throw new LoginException();
    }
    return user;
  }

  public User getUser(final String userId) {
    return this.userService.getUserByUserId(userId);
  }

  public JwtInfo getJwtInfo(final String userId) {
    return this.jwtService.getJwtInfo(userId);
  }
}
