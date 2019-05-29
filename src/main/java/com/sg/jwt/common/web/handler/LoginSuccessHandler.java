package com.sg.jwt.common.web.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sg.jwt.common.jwt.JwtInfo;
import com.sg.jwt.common.model.SecurityUser;
import com.sg.jwt.common.util.JwtUtil;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		// TODO Auto-generated method stub
		SecurityUser securityUser = Optional.ofNullable((SecurityUser) authentication.getPrincipal()).get();
		String token = JwtUtil.createToken(securityUser);
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

		Map<String, Object> values = new HashMap<>();
		values.put("user", securityUser);
		values.put(JwtInfo.TOKEN_NAME, token);

		new ObjectMapper().writeValue(response.getWriter(), values);
	}
}
