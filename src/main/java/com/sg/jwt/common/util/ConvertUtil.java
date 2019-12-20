package com.sg.jwt.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.sg.jwt.common.model.LoginUser;
import com.sg.jwt.common.model.SecurityUser;
import com.sg.jwt.common.web.details.TokenDetails;

public class ConvertUtil {

	public static LoginUser convertLoginUser(Object obj) {
		if (obj instanceof SecurityUser) {
			return ((SecurityUser) obj).getLoginUser();
		} else {
			throw new RuntimeException("Bad Object");
		}
	}

	public static TokenDetails convertTokenDetails(Object obj) {
		if (obj instanceof TokenDetails) {
			return (TokenDetails) obj;
		} else {
			throw new RuntimeException("Bad Object");
		}
	}

	public static Collection<? extends GrantedAuthority> convertAuthorities(String[] roles) {
		List<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
		for (String role : roles) {
			auth.add(new SimpleGrantedAuthority(role));
		}
		return auth;
	}
}
