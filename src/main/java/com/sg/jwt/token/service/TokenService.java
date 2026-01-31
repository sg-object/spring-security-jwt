package com.sg.jwt.token.service;

import com.sg.jwt.exception.RefreshTokenException;
import com.sg.jwt.token.model.JwtInfo;
import com.sg.jwt.web.value.HeaderKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Service
public class TokenService {

  private final JwtService jwtService;

  public JwtInfo reissueToken(final HttpHeaders headers) {
    final var accessClaims = getAccessTokenClaims(headers);
    final var jti = accessClaims.getId();
    if (!jti.equals(getRefreshTokenJti(headers))) {
      throw new RefreshTokenException();
    }
    final var accessToken = this.jwtService.createAccessToken(jti, this.jwtService.getUserIdFromClaims(accessClaims));
    return JwtInfo.builder().accessToken(accessToken).build();
  }

  private Claims getAccessTokenClaims(final HttpHeaders headers) {
    final var accessToken = this.jwtService.getAuthorizationToken(headers.getFirst(HeaderKey.AUTHORIZATION));
    return this.jwtService.decode(accessToken);
  }

  private String getRefreshTokenJti(final HttpHeaders headers) {
    return verifyRefreshToken(headers).getPayload().getId();
  }

  private Jws<Claims> verifyRefreshToken(final HttpHeaders headers) {
    final var refreshToken = headers.getFirst(HeaderKey.REFRESH_TOKEN);
    if (!StringUtils.hasText(refreshToken)) {
      throw new RefreshTokenException();
    }
    return this.jwtService.verify(refreshToken);
  }
}
