package com.sg.jwt.web.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginUser {

  @NotBlank
  private String id;

  @NotBlank
  private String password;
}
