package com.sg.jwt.user.service;

import com.sg.jwt.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

  public User getUserByUserId(final String userId) {
    return User.builder().password("$2a$10$9rVvCg7fDYAJDOlhZTtDiu2SS2vaL4iCWNgYgTkVT9gz9f0bln0ZS").name("sg").build();
  }
}
