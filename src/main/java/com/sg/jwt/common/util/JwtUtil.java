package com.sg.jwt.common.util;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import org.joda.time.DateTime;
import org.springframework.security.authentication.BadCredentialsException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sg.jwt.common.jwt.ClaimField;
import com.sg.jwt.common.jwt.JwtInfo;
import com.sg.jwt.common.model.SecurityUser;

public abstract class JwtUtil {

	public static String createToken(SecurityUser securityUser) throws UnsupportedEncodingException {
		return createToken(securityUser, JodaTimeUtil.nowAfterDaysToDate(JwtInfo.EXPIRES_LIMIT));
	}

	public static String createToken(SecurityUser securityUser, DateTime expiresAt)
			throws UnsupportedEncodingException {
		String token = JWT.create().withIssuer(JwtInfo.ISSUER)
				.withClaim(ClaimField.id.name(), securityUser.getUsername())
				.withClaim(ClaimField.name.name(), securityUser.getName())
				.withArrayClaim(ClaimField.roles.name(),
						securityUser.getAuthorities().stream().map(role -> role.getAuthority()).toArray(String[]::new))
				.withExpiresAt(expiresAt.toDate()).sign(JwtInfo.getAlgorithm());
		return Optional.ofNullable(token).get();
	}

	public static Boolean verify(String token) throws UnsupportedEncodingException {
		try {
			JWT.require(JwtInfo.getAlgorithm()).build().verify(token);
			return Boolean.TRUE;
		} catch (JWTVerificationException e) {
			// TODO Auto-generated catch block
			return Boolean.FALSE;
		}
	}

	public static DecodedJWT decodeToken(String token) {
		return Optional.ofNullable(JWT.decode(token)).orElseThrow(() -> new BadCredentialsException("Not used Token"));
	}
}
