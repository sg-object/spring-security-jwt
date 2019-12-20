package com.sg.jwt.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class LoginUser {

	private Long id;

	private String loginId;

	private String password;

	private String name;

	private String email;

	private String[] roles;
	
	private boolean autoLogin;

	public LoginUser(){}
	
	public LoginUser(SecurityUser securityUser){
		this.loginId = securityUser.getUsername();
		this.name = securityUser.getName();
		this.roles = securityUser.getAuthorities().stream().map(role -> role.getAuthority()).toArray(String[]::new);
	}
}
