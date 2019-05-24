package com.sg.jwt.common.web.service;

import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import com.sg.jwt.common.model.LoginUser;
import com.sg.jwt.common.model.SecurityUser;

@Service
public class AjaxUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) {
		// TODO Auto-generated method stub
		// db 조회로 사용자 정보 추출
		// test를 위해 임시로 인스턴스 생성
		LoginUser loginUser = new LoginUser();
		loginUser.setLoginId("test");
		loginUser.setPassword("test");

		return Optional.ofNullable(loginUser).filter(user -> user != null).map(user -> new SecurityUser(user)).get();
	}
}
