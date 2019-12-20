package com.sg.jwt.common.web.service;

import java.io.UnsupportedEncodingException;
import org.springframework.stereotype.Service;
import com.sg.jwt.common.jwt.ClaimField;
import com.sg.jwt.common.jwt.JwtInfo;
import com.sg.jwt.common.model.LoginUser;
import com.sg.jwt.common.util.JodaTimeUtil;
import io.jsonwebtoken.Jwts;

@Service
public class TokenService {

	public String getJwtToken(LoginUser loginUser) {
		try {
			return Jwts.builder().setIssuer(JwtInfo.ISSUER)
					.claim(ClaimField.id.name(), loginUser.getLoginId())
					.claim(ClaimField.name.name(), loginUser.getName())
					.claim(ClaimField.roles.name(), loginUser.getRoles())
					.setExpiration(JodaTimeUtil.nowAfterHoursToDate(JwtInfo.EXPIRES_LIMIT).toDate())
					.signWith(JwtInfo.ALGORITHM, JwtInfo.getSecretKey()).compact();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public String getRefreshToken(LoginUser loginUser) {
		// Create Refresh Token Process
		return "test refresh";
	}

	public String checkExpiredRefreshToken(LoginUser loginUser) {
		// Check Refresh Token | Update Refresh Token
		return "new refresh";
	}
}
