# spring-security-jwt

## 주요 기능
* Spring Security, JWT 연동
* JWT 발급 및 검사

## Version
* Spring Boot : 3.5.9
* JJWT : 0.13.0

## LoginAuthenticationFilter
* 로그인 API Filter
* Filter에서는 Unauthenticated Token만 생성
* 로그인 처리 및 JWT 발급은 **LoginAuthenticationProvider**에서 처리
```java
public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  private final LoginService loginService;

  public LoginAuthenticationFilter(final RequestMatcher requestMatcher, final LoginService loginService) {
    super(requestMatcher);
    this.loginService = loginService;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    final var user = this.loginService.getLoginUser(request);
    return getAuthenticationManager().authenticate(UsernamePasswordAuthenticationToken.unauthenticated(user.getId(), user.getPassword()));
  }
}
```

## LoginAuthenticationProvider
* LoginAuthenticationFilter에서 생성한 Unauthenticated Token에서 로그인 정보 추출
* 해당 정보로 로그인 처리 (사용자는 Hard Coding 으로 처리)
* 로그인이 정상적으로 완료되면 JWT 발급 (AccessToken & RefreshToken)
```java
@RequiredArgsConstructor
public class LoginAuthenticationProvider implements AuthenticationProvider {

  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  private final LoginService loginService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    final var userId = authentication.getPrincipal().toString();
    final var password = authentication.getCredentials().toString();

    final var user = this.loginService.getUser(userId);
    if (!this.passwordEncoder.matches(password, user.getPassword())) {
      throw new LoginException();
    }

    final var result = UsernamePasswordAuthenticationToken.authenticated(userId, "", null);
    result.setDetails(this.loginService.getJwtInfo(userId));

    return result;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
```

## JwtAuthenticationFilter
* Request Header에서 JWT 기반 AccessToken 추출 및 검증 처리
* JWT 검증 (만료 및 서명)
* 검증 완료 후 Claim에서 사용자 정보를 추출
* Authenticated Token에 사용자 정보를 설정하고 SecurityContextHolder에 해당 Token을 저장
```java
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  private final HandlerExceptionResolver handlerExceptionResolver;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      final var accessToken = this.jwtService.getAuthorizationToken(request.getHeader(HeaderKey.AUTHORIZATION));
      final var claims = this.jwtService.verify(accessToken);
      final var user = new UserInfo(this.jwtService.getUserIdFromClaims(claims.getPayload()));
      SecurityContextHolder.getContext().setAuthentication(UsernamePasswordAuthenticationToken.authenticated(user, "", null));
      filterChain.doFilter(request, response);
    } catch (final Exception e) {
      this.handlerExceptionResolver.resolveException(request, response, null, e);
    }
  }
}
```

## 테스트
* JUnit 기반 테스트
* test package의 **SpringSecurityJwtApplicationTests**
```java
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
  
  //...........
}
```
