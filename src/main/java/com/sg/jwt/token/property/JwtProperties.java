package com.sg.jwt.token.property;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("jwt")
@RequiredArgsConstructor
@Validated
@Getter
public class JwtProperties {

  @NotBlank
  private final String secretKey;

  @NotBlank
  private final String issuer;

  @Valid
  @NotNull
  private final Expiration expiration;

  @RequiredArgsConstructor
  @Getter
  public static class Expiration {

    @Min(1)
    private final int accessToken;

    @Min(1)
    private final int refreshToken;
  }
}
