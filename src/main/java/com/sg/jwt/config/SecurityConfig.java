package com.sg.jwt.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sg.jwt.token.service.JwtService;
import com.sg.jwt.web.filter.JwtAuthenticationFilter;
import com.sg.jwt.web.filter.LoginAuthenticationFilter;
import com.sg.jwt.web.handler.LoginFailureHandler;
import com.sg.jwt.web.handler.LoginSuccessHandler;
import com.sg.jwt.web.provider.LoginAuthenticationProvider;
import com.sg.jwt.web.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.servlet.HandlerExceptionResolver;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

  private final LoginService loginService;

  private final JwtService jwtService;

  private final ObjectMapper objectMapper;

  private final AuthenticationConfiguration authenticationConfiguration;

  private final HandlerExceptionResolver handlerExceptionResolver;

  private final String LOGIN_PATH = "/login";

  @Bean
  WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring().requestMatchers("/token");
  }

  @Bean
  SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable).formLogin(AbstractHttpConfigurer::disable);
    http.authorizeHttpRequests(req -> req.requestMatchers(this.LOGIN_PATH).permitAll().anyRequest().authenticated());
    http.addFilterBefore(loginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(jwtAuthenticationFilter(), LoginAuthenticationFilter.class);
    return http.build();
  }

  private LoginAuthenticationFilter loginAuthenticationFilter() throws Exception {
    final var filter = new LoginAuthenticationFilter(PathPatternRequestMatcher.withDefaults().matcher(this.LOGIN_PATH), this.loginService);
    filter.setAuthenticationManager(this.authenticationConfiguration.getAuthenticationManager());
    filter.setAuthenticationSuccessHandler(new LoginSuccessHandler(this.objectMapper));
    filter.setAuthenticationFailureHandler(new LoginFailureHandler(this.handlerExceptionResolver));
    return filter;
  }

  private JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter(this.jwtService, this.handlerExceptionResolver);
  }

  @Bean
  public LoginAuthenticationProvider loginAuthenticationProvider() {
    return new LoginAuthenticationProvider(this.loginService);
  }
}
