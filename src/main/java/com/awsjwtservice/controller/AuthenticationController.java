/**
 * 
 */
package com.awsjwtservice.controller;

import static org.springframework.http.ResponseEntity.ok;

import java.util.ArrayList;
import java.util.List;

import com.awsjwtservice.config.security.JwtAuthenticationService;
import com.awsjwtservice.repository.UserRepository;
import com.awsjwtservice.dto.AccountCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;


@RestController
public class AuthenticationController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtAuthenticationService jwtAuthenticationService;

	@Autowired
	UserRepository userRepository;

	@PostMapping("/auth/login")
	public String signin(HttpServletResponse response, @RequestBody AccountCredentials credentials, Model model) {

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));

			List<String> list = new ArrayList<>();

			System.out.println(credentials + ": credentials");

			list.add(this.userRepository.findByUsername(credentials.getUsername()).getRole());

			String token = jwtAuthenticationService.createToken(credentials.getUsername(), list);

			response.setHeader("token", token);
			response.setHeader("username", credentials.getUsername());


			return "index";
		} catch (AuthenticationException e) {
			throw new BadCredentialsException("Invalid username/password supplied");
		}
	}
}
