package com.sg.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sg.jwt.web.value.HeaderKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;
import java.util.HashMap;
import java.util.Map;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class SpringSecurityJwtApplicationTests {

  @Autowired
  private MockMvc mvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @DisplayName("로그인 테스트")
  @Test
  void login() throws Exception {
    final var body = new HashMap<String, String>();
    body.put("id", "test");
    body.put("password", "test");

    final var content = this.objectMapper.writeValueAsString(body);
    final var res = this.mvc.perform(
                    post("/login").contentType(MediaType.APPLICATION_JSON).content(content))
            .andDo(print()).andExpect(status().isOk()).andReturn();
    final var map = this.objectMapper.readValue(res.getResponse().getContentAsString(), Map.class);

    Assert.notNull(map.get("accessToken"), "");
    Assert.notNull(map.get("refreshToken"), "");
  }

  @DisplayName("토큰 만료 테스트")
  @Test
  void expiredToken() throws Exception {
    final var token = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJzZyIsImp0aSI6ImQxZTk5ZGUzLTAzN2MtNDBkZS1hNzg1LWQzYWEzNTUwNThmYyIsInVzZXJfaWQiOiJ0ZXN0IiwiZXhwIjoxNzY5ODYxNTY1fQ.ubBNK0nUxFvXeVC_QPo9V4w4FRn9BykdsDByY8bhHCI";

    this.mvc.perform(
                    get("/test").header(HeaderKey.AUTHORIZATION, HeaderKey.TOKEN_PREFIX + token))
            .andDo(print()).andExpect(status().isForbidden());
  }

  @DisplayName("토큰 검증 테스트 (다른 SecretKey)")
  @Test
  void validToken() throws Exception {
    final var token = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJzZyIsImp0aSI6ImE5NDM3M2JiLWQxN2ItNDkyMy04OGM1LThjYjk4NGExNTI5ZiIsInVzZXJfaWQiOiJ0ZXN0IiwiZXhwIjoxNzY5ODYyNzE3fQ.WeyUQSNXMMQ8DDs3aorCKqOpy7zDR5jJFRrAvDcE_64";

    this.mvc.perform(
                    get("/test").header(HeaderKey.AUTHORIZATION, HeaderKey.TOKEN_PREFIX + token))
            .andDo(print()).andExpect(status().isUnauthorized());
  }

  @DisplayName("토큰 재발급 테스트")
  @Test
  void reissueToken() throws Exception {
    final var accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJzZyIsImp0aSI6IjZmMGFkMzFjLTllZTctNGM3OC1hMmM3LTJjNjhhN2U4MWU5NCIsInVzZXJfaWQiOiJ0ZXN0IiwiZXhwIjoxNzY5ODYzMDQzfQ.8rqGagEhIilHuMedS63fvSSmr3d0NlJuk5a0xqjl5hE";
    final var refreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJzZyIsImp0aSI6IjZmMGFkMzFjLTllZTctNGM3OC1hMmM3LTJjNjhhN2U4MWU5NCIsImV4cCI6MTc2OTg2NjM0M30.hpfSjpsN1x3vcBLvjzm7Vo-DDnLog7QYLeljtcR2dAA";

    final var res = this.mvc.perform(
                    post("/token")
                            .header(HeaderKey.AUTHORIZATION, HeaderKey.TOKEN_PREFIX + accessToken)
                            .header(HeaderKey.REFRESH_TOKEN, refreshToken))
            .andDo(print()).andExpect(status().isOk()).andReturn();
    final var map = this.objectMapper.readValue(res.getResponse().getContentAsString(), Map.class);

    Assert.notNull(map.get("accessToken"), "");
  }
}
