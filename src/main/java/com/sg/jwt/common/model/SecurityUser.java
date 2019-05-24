package com.sg.jwt.common.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SecurityUser extends User{

	private static final long serialVersionUID = -3531439484732724601L;
	
	public SecurityUser(LoginUser loginUser) {
		// TODO Auto-generated constructor stub
		super(loginUser.getLoginId(), loginUser.getPassword(), getGrantedAuthority());
	}
	
	private static List<GrantedAuthority> getGrantedAuthority(){
		List<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
	    auth.add(new SimpleGrantedAuthority("ROLE_USER"));
	    return auth;
	}
}
