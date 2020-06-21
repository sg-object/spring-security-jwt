package com.sg.jwt.common.web.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.sg.jwt.common.model.SecurityUser;
import com.sg.jwt.common.util.ConvertUtil;
import com.sg.jwt.user.service.UserService;

@Service
public class AjaxUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) {
		// TODO Auto-generated method stub
		return Optional.ofNullable(userService.getLoginUser(username)).filter(user -> user != null)
				.map(user -> new SecurityUser(user, ConvertUtil.convertAuthorities(user.getRoles())))
				.orElseThrow(() -> new UsernameNotFoundException(username));
	}
}
