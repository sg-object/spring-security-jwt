package com.sg.jwt.common.web.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
import com.sg.jwt.common.model.LoginUser;
import com.sg.jwt.common.util.ConvertUtil;
import com.sg.jwt.common.web.details.TokenDetails;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		// TODO Auto-generated method stub
		LoginUser loginUser = ConvertUtil.convertLoginUser(authentication.getPrincipal());
		TokenDetails tokenDetails = ConvertUtil.convertTokenDetails(authentication.getDetails());
		
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

		Map<String, Object> values = new HashMap<>();
		values.put("id", loginUser.getLoginId());
		values.put("name", loginUser.getName());
		values.put(JwtInfo.TOKEN_NAME, tokenDetails.getToken());
		if(tokenDetails.isAutoLogin()){
			values.put(JwtInfo.REFRESH_TOKEN, tokenDetails.getRefreshToken());
		}

		new ObjectMapper().writeValue(response.getWriter(), values);
	}
}
