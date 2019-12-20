package com.sg.jwt.common.util;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import org.joda.time.DateTime;
import com.sg.jwt.common.jwt.ClaimField;
import com.sg.jwt.common.jwt.JwtInfo;
import com.sg.jwt.common.model.LoginUser;
import io.jsonwebtoken.Jwts;

@Deprecated
public abstract class JwtUtil {

	public static String createToken(LoginUser loginUser) throws UnsupportedEncodingException {
		return createToken(loginUser, JodaTimeUtil.nowAfterDaysToDate(JwtInfo.EXPIRES_LIMIT));
	}

	public static String createToken(LoginUser loginUser, DateTime expiresAt)
			throws UnsupportedEncodingException {
		String token = Jwts.builder()
				.setIssuer(JwtInfo.ISSUER)
				.claim(ClaimField.id.name(), loginUser.getLoginId())
				.claim(ClaimField.name.name(), loginUser.getName())
				.claim(ClaimField.roles.name(), loginUser.getRoles())
				.setExpiration(expiresAt.toDate())
				.signWith(JwtInfo.ALGORITHM, JwtInfo.getSecretKey())
				.compact();

		return Optional.ofNullable(token).get();
	}

}
