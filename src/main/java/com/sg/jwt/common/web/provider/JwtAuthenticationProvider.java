package com.sg.jwt.common.web.provider;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import com.sg.jwt.common.jwt.ClaimField;
import com.sg.jwt.common.jwt.JwtAuthenticationToken;
import com.sg.jwt.common.jwt.JwtInfo;
import com.sg.jwt.common.util.ConvertUtil;
import com.sg.jwt.common.web.details.TokenDetails;
import com.sg.jwt.common.web.service.JwtUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication) {
		// TODO Auto-generated method stub
		TokenDetails details = ConvertUtil.convertTokenDetails(authentication.getDetails());
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(JwtInfo.getSecretKey()).parseClaimsJws(details.getToken());
			return getJwtAuthenticationToken(claims.getBody());
		} catch (ExpiredJwtException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return jwtUserDetailsService.updateJwtToken(details);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private JwtAuthenticationToken getJwtAuthenticationToken(Claims body) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		((ArrayList<String>) body.get(ClaimField.roles.name())).forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role));
		});
		return new JwtAuthenticationToken(body.get(ClaimField.id.name()), body.get(ClaimField.name.name()),
				authorities);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return JwtAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
