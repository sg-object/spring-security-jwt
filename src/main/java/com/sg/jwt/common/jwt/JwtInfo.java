package com.sg.jwt.common.jwt;

import java.io.UnsupportedEncodingException;
import com.auth0.jwt.algorithms.Algorithm;

public abstract class JwtInfo {

	public static final String TOKEN_NAME = "jwt-token";

	public static final String ISSUER = "sg.lee";

	public static final String SECRET_KEY = "secret";

	public static final int EXPIRES_LIMIT = 3;

	public static Algorithm getAlgorithm() throws UnsupportedEncodingException {
		try {
			return Algorithm.HMAC256(JwtInfo.SECRET_KEY);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			return Algorithm.none();
		}
	}
}
