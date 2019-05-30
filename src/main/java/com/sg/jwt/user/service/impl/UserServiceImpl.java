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
		user.setPassword("$2a$10$F01fil5fQOQKDLV0Szq.JuUgznfKYnYGxxclEcW0X6d7FPA8PvneK");
		user.setName("my_name");
		user.setRoles(new String[] { "ROLE_ADMIN", "ROLE_USER" });
		return user;
	}
}
