package com.sg.jwt.user.service.impl;

import org.springframework.stereotype.Service;
import com.sg.jwt.common.model.LoginUser;
import com.sg.jwt.user.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Override
	public LoginUser getLoginUser(String loginId) {
		// TODO Auto-generated method stub
		LoginUser user = new LoginUser();
		user.setLoginId(loginId);
		user.setPassword("$2a$10$aiC.l2gfQzHO..bUDnbhS.3i90lA8V8P0xjbk92CywDhJP3IK5Bcq"); // value is test
		user.setName("my_name");
		user.setRoles(new String[] { "ROLE_ADMIN", "ROLE_USER" });
		return user;
	}
	
	@Override
	public LoginUser getLoginUserByRefreshToken(String refreshToken) {
		// TODO Auto-generated method stub
		return getLoginUser("test");
	}
}
