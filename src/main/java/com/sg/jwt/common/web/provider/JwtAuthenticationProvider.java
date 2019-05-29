package com.sg.jwt.common.web.provider;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sg.jwt.common.jwt.ClaimField;
import com.sg.jwt.common.jwt.JwtAuthenticationToken;
import com.sg.jwt.common.model.SecurityUser;
import com.sg.jwt.common.util.JwtUtil;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// TODO Auto-generated method stub
		String token = Optional.ofNullable(authentication.getCredentials())
				.orElseThrow(() -> new BadCredentialsException("Bad token")).toString();

		try {
			if (JwtUtil.verify(token)) {
				SecurityUser securityUser = getSecurityUser(token);
				return new JwtAuthenticationToken(securityUser.getUsername(), securityUser,
						securityUser.getAuthorities());
			} else {
				throw new BadCredentialsException("Bad token");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BadCredentialsException("Bad token");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return JwtAuthenticationToken.class.isAssignableFrom(authentication);
	}

	private SecurityUser getSecurityUser(String token) {
		DecodedJWT decodedJWT = JwtUtil.decodeToken(token);
		String[] roles = decodedJWT.getClaim(ClaimField.roles.name()).asArray(String.class);
		return new SecurityUser(decodedJWT.getClaim(ClaimField.id.name()).asString(),
				decodedJWT.getClaim(ClaimField.name.name()).asString(),
				Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
	}
}
