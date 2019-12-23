package com.sg.jwt.common.web.provider;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.sg.jwt.common.model.LoginUser;
import com.sg.jwt.common.util.ConvertUtil;
import com.sg.jwt.common.web.details.TokenDetails;
import com.sg.jwt.common.web.service.AjaxUserDetailsService;
import com.sg.jwt.common.web.service.TokenService;

@Component
public class AjaxAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AjaxUserDetailsService ajaxUserDetailsService;
	@Autowired
	private TokenService tokenService;

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		// TODO Auto-generated method stub
		Optional.ofNullable(authentication.getCredentials())
				.filter(password -> passwordEncoder.matches(password.toString(), userDetails.getPassword()))
				.orElseThrow(() -> new BadCredentialsException("Bad credentials"));
		LoginUser loginUser = ConvertUtil.convertLoginUser(userDetails);
		TokenDetails tokenDetails = ConvertUtil.convertTokenDetails(authentication.getDetails());
		setTokens(tokenDetails, loginUser);
	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		// TODO Auto-generated method stub
		return ajaxUserDetailsService.loadUserByUsername(username);
	}

	private void setTokens(TokenDetails tokenDetails, LoginUser loginUser) {
		if (tokenDetails.isAutoLogin()) {
			tokenDetails.setRefreshToken(tokenService.getRefreshToken(loginUser));
		}
		tokenDetails.setToken(tokenService.getJwtToken(loginUser));
	}
}
