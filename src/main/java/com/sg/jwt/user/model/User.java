package com.sg.jwt.user.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class User {

  private String password;

  private String name;
}
