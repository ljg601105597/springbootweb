package com.ljg;

import com.ljg.common.TokenManager;
import com.ljg.exception.TokenException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SpringBootApplication
@RestController
public class SpringbootWebApplication {

	private int expireSeconds=60*2;//2分钟

	public static void main(String[] args) {
		SpringApplication.run(SpringbootWebApplication.class, args);
	}

	@RequestMapping("/hello")
	public String hello(HttpServletRequest request, HttpServletResponse response) throws TokenException {
		Cookie cookie = new Cookie("cookiename","cookievalue");
		response.addCookie(cookie);
		return "no hello world";
	}

	@RequestMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response) throws TokenException {

		String loginname=request.getParameter("loginname");
		String password=request.getParameter("password");

		String token=TokenManager.createJWT(loginname,"李金国","123",expireSeconds);

		Cookie cookie = new Cookie("token",token);
//		cookie.setMaxAge(expireSeconds);
		response.addCookie(cookie);
		return token;
	}
}
