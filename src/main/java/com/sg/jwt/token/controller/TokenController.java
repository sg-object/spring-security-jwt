package com.sg.jwt.token.controller;

import com.sg.jwt.token.model.JwtInfo;
import com.sg.jwt.token.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("token")
@RequiredArgsConstructor
@RestController
public class TokenController {

  private final TokenService tokenService;

  @PostMapping
  public JwtInfo reissueToken(@RequestHeader final HttpHeaders headers) {
    return this.tokenService.reissueToken(headers);
  }
}
