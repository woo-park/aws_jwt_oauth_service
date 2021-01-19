package com.awsjwtservice.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	
	@RequestMapping({ "/user", "/me" })
	public Principal user(Principal principal, HttpServletRequest request) {
		System.out.println("Authorization Header Value ::"+request.getHeader("Authorization"));
		return principal;
	}
}
