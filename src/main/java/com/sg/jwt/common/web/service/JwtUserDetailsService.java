package com.sg.jwt.common.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.sg.jwt.common.jwt.JwtAuthenticationToken;
import com.sg.jwt.common.model.LoginUser;
import com.sg.jwt.common.util.ConvertUtil;
import com.sg.jwt.common.web.details.TokenDetails;
import com.sg.jwt.user.service.UserService;

@Component
public class JwtUserDetailsService {

	@Autowired
	private TokenService tokenService;
	@Autowired
	private UserService userService;

	public JwtAuthenticationToken updateJwtToken(TokenDetails details) {
		String refreshToken = details.getRefreshToken();
		if (refreshToken == null || "".equals(refreshToken)) {
			// Expired Jwt Token
			throw new RuntimeException();
		}

		LoginUser loginUser = userService.getLoginUserByRefreshToken(refreshToken);
		if (loginUser == null) {
			// Not Found User & Expired Jwt Token
			throw new RuntimeException();
		}

		// Update Token
		details.setRefreshToken(tokenService.checkExpiredRefreshToken(loginUser));
		details.setToken(tokenService.getJwtToken(loginUser));
		details.setUpdatedToken(true);

		return new JwtAuthenticationToken(loginUser.getLoginId(), loginUser.getName(),
				ConvertUtil.convertAuthorities(loginUser.getRoles()));
	}

}
