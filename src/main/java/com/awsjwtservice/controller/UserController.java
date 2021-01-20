package com.awsjwtservice.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController

@Controller
public class UserController {
	
	@RequestMapping({ "/user", "/me" })
	public Principal user(Principal principal, HttpServletRequest request) {
		System.out.println("Authorization Header Value ::"+request.getHeader("Authorization"));
		return principal;
	}

	@GetMapping("/")
	public String home(Model model) throws Exception {
		return "index";
	}

	@GetMapping("/mypage")
	public String mypage(Model model) throws Exception {
		return "mypage";
	}


}
