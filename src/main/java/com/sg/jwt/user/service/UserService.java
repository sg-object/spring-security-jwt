package com.sg.jwt.user.service;

import com.sg.jwt.common.model.LoginUser;

public interface UserService {

	public LoginUser getLoginUser(String loginId);
}
