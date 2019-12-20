package com.sg.jwt.common.web.details;

import com.sg.jwt.common.model.LoginUser;
import lombok.Data;

@Data
public class TokenDetails {

	private boolean autoLogin;

	private String token;

	private String refreshToken;

	private boolean updatedToken;

	public TokenDetails() {
	}

	public TokenDetails(LoginUser loginUser) {
		this.autoLogin = loginUser.isAutoLogin();
	}
}
