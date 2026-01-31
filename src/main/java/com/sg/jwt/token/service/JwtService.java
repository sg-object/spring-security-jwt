package com.sg.jwt.token.service;

import com.sg.jwt.token.model.JwtInfo;
import com.sg.jwt.token.property.JwtProperties;
import com.sg.jwt.token.value.ClaimsKey;
import com.sg.jwt.web.value.HeaderKey;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

  private final JwtProperties jwtProperties;

  private final SecretKey secretKey;

  private final JwtParser jwtParser;

  public JwtService(final JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties;
    this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
    this.jwtParser = Jwts.parser().verifyWith(this.secretKey).build();
  }

  public String getAuthorizationToken(final String authorization) {
    final var prefix = HeaderKey.TOKEN_PREFIX;
    if (authorization == null || !authorization.startsWith(prefix)) {
      throw new RuntimeException();
    }
    return authorization.substring(prefix.length());
  }

  public Jws<Claims> verify(final String token) throws ExpiredJwtException, SignatureException {
    return this.jwtParser.parseSignedClaims(token);
  }

  public Claims decode(final String token) throws SignatureException {
    try {
      return this.jwtParser.parseSignedClaims(token).getPayload();
    } catch (final ExpiredJwtException e) {
      return e.getClaims();
    }
  }

  public String getUserIdFromClaims(final Claims claims) {
    return claims.get(ClaimsKey.USER_ID, String.class);
  }

  public JwtInfo getJwtInfo(final String userId) {
    final var jti = createJti();
    return JwtInfo.builder().accessToken(createAccessToken(jti, userId)).refreshToken(createRefreshToken(jti)).build();
  }

  public String createAccessToken(final String jti, final String userId) {
    final var expiration = getExpiration(this.jwtProperties.getExpiration().getAccessToken());
    return getJwtBuilder(jti).claim(ClaimsKey.USER_ID, userId).expiration(expiration).compact();
  }

  private String createRefreshToken(final String jti) {
    final var expiration = getExpiration(this.jwtProperties.getExpiration().getRefreshToken());
    return getJwtBuilder(jti).expiration(expiration).compact();
  }

  private JwtBuilder getJwtBuilder(final String jti) {
    return Jwts.builder().signWith(this.secretKey).issuer(this.jwtProperties.getIssuer()).id(jti);
  }

  private Date getExpiration(final int seconds) {
    return Date.from(LocalDateTime.now().plusSeconds(seconds).atZone(ZoneId.systemDefault()).toInstant());
  }

  private String createJti() {
    return UUID.randomUUID().toString();
  }
}
