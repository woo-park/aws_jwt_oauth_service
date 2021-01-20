package com.awsjwtservice.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.awsjwtservice.config.annotation.LoginUser;
import com.awsjwtservice.dto.SessionUserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//@RestController

@Controller
public class UserController {

	private final HttpSession httpSession;

	public UserController(HttpSession httpSession) {
		this.httpSession = httpSession;
	}


	@RequestMapping({ "/user", "/me" })
	public Principal user(Principal principal, HttpServletRequest request) {
		System.out.println("Authorization Header Value ::"+request.getHeader("Authorization"));
		return principal;
	}

	@GetMapping("/")
	public String home(Model model, @LoginUser SessionUserDto user) throws Exception {
		if(user != null) {
			model.addAttribute("name", user.getUsername());
			model.addAttribute("email", user.getEmail());
		}

		return "index";
	}

	@GetMapping("/mypage")
	public String mypage(Model model, HttpServletRequest request) throws Exception {


		SessionUserDto user = (SessionUserDto) httpSession.getAttribute("user");

		if(user != null){
			model.addAttribute("username", user.getUsername());
			model.addAttribute("useremail", user.getEmail());
		}



		return "mypage";
	}


}
