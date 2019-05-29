package com.sg.jwt.common.model;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(Include.NON_NULL)
public class SecurityUser extends User {

	private static final long serialVersionUID = -3531439484732724601L;

	private String name;

	public SecurityUser(LoginUser loginUser, Collection<? extends GrantedAuthority> authorities) {
		// TODO Auto-generated constructor stub
		super(loginUser.getLoginId(), loginUser.getPassword(), authorities);
		this.name = loginUser.getName();
	}

	public SecurityUser(String loginId, String name, Collection<? extends GrantedAuthority> authorities) {
		super(loginId, "password", authorities);
		this.name = name;
	}
}
