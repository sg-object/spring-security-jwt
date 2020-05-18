package com.sg.jwt;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.Data;

@RestController
public class TestController {

	@PostMapping("/login")
	public void login(@RequestBody SwaggerUser user) {

	}

	@GetMapping("/test")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "jwt-token", required = true, paramType="header"),
		@ApiImplicitParam(name = "refresh-token", required = false, paramType="header")
	})
	public String test() {
		return "success!!!!";
	}
	
	// Swagger Test Vo
	@Data
	class SwaggerUser{
		
		private String loginId;
		
		private String password;
		
		private boolean autoLogin;
	}
}
