package com.sg.jwt.common.web.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import com.sg.jwt.common.jwt.JwtAuthenticationToken;
import com.sg.jwt.common.jwt.JwtInfo;
import com.sg.jwt.common.util.ConvertUtil;
import com.sg.jwt.common.web.details.TokenDetails;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	public JwtAuthenticationFilter(RequestMatcher requestMatcher) {
		// TODO Auto-generated constructor stub
		super(requestMatcher);
	}

	private TokenDetails getTokenDetails(String token, String refreshToken) {
		TokenDetails details = new TokenDetails();
		details.setToken(token);
		if (refreshToken != null) {
			details.setRefreshToken(refreshToken);
		}
		return details;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		// TODO Auto-generated method stub
		String token = request.getHeader(JwtInfo.TOKEN_NAME);
		String refreshToken = request.getHeader(JwtInfo.REFRESH_TOKEN);

		if (StringUtils.isEmpty(token)) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		} else {
			return getAuthenticationManager()
					.authenticate(new JwtAuthenticationToken(token, getTokenDetails(token, refreshToken)));
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		// TODO Auto-generated method stub
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authResult);
		SecurityContextHolder.setContext(context);
		TokenDetails details = ConvertUtil.convertTokenDetails(authResult.getDetails());
		if(details.isUpdatedToken()){
			response.setHeader(JwtInfo.TOKEN_NAME, details.getToken());
			response.setHeader(JwtInfo.REFRESH_TOKEN, details.getRefreshToken());
		}
		chain.doFilter(request, response);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		// TODO Auto-generated method stub
		SecurityContextHolder.clearContext();
		getFailureHandler().onAuthenticationFailure(request, response, failed);
	}
}
