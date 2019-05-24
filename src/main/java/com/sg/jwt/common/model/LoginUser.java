package com.sg.jwt.common.model;

import lombok.Data;

@Data
public class LoginUser {

	private Long id;
	
	private String loginId;
	
	private String password;
	
	private String email;
}
