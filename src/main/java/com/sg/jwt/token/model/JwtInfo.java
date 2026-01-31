package com.sg.jwt.token.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class JwtInfo {

  private String accessToken;

  private String refreshToken;
}
