package com.sg.jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.sg.jwt.common.web.filter.AjaxAuthenticationFilter;
import com.sg.jwt.common.web.handler.BaseFailureHandler;
import com.sg.jwt.common.web.handler.BaseSuccessHandler;
import com.sg.jwt.common.web.provider.AjaxAuthenticationProvider;

@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AjaxAuthenticationProvider ajaxProvider;
	@Autowired
	private BaseSuccessHandler baseSuccessHandler;
	@Autowired
	private BaseFailureHandler baseFailureHandler;

	private static final String ROOT_ENTRY_POINT = "/**";
	private static final String LOGIN_ENTRY_POINT = "/login";

	@Override
	public void configure(WebSecurity web) throws Exception {
		// TODO Auto-generated method stub
		web.ignoring().antMatchers("/resources/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http.authorizeRequests().antMatchers(LOGIN_ENTRY_POINT).permitAll().anyRequest().authenticated();
		http.addFilterBefore(ajaxAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class).csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		auth.authenticationProvider(ajaxProvider);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AntPathRequestMatcher antPathRequestMatcher() {
		return new AntPathRequestMatcher(LOGIN_ENTRY_POINT, HttpMethod.POST.name());
	}

	@Bean
	public AjaxAuthenticationFilter ajaxAuthenticationFilter() throws Exception {
		AjaxAuthenticationFilter filter = new AjaxAuthenticationFilter(antPathRequestMatcher());
		filter.setAuthenticationManager(authenticationManager());
		filter.setAuthenticationSuccessHandler(baseSuccessHandler);
		filter.setAuthenticationFailureHandler(baseFailureHandler);
		return filter;
	}
}
