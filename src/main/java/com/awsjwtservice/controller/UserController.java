package com.awsjwtservice.controller;

import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.awsjwtservice.config.annotation.LoginUser;
import com.awsjwtservice.domain.Account;
import com.awsjwtservice.dto.SessionUserDto;
import com.awsjwtservice.service.AccountService;
import com.awsjwtservice.service.UserPDFExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;



import com.lowagie.text.DocumentException;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {

	private final HttpSession httpSession;

	// get a logger
	private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserController.class);

	@Autowired
	AccountService accountService;

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

			String role = user.getRole();
			if(role.equals("ROLE_ADMIN")) {
				model.addAttribute("admin", true);
			} else if (role.equals("ROLE_MANAGER")) {
				model.addAttribute("manager", true);
			}

			logger.info(user.getUsername() + "reached" + ": \"/\"");
		} else {
			// log
			logger.info("reached" + ": \"/\"");
		}



		return "index";
	}

	@GetMapping("/mypage")
	public String mypage(Model model, HttpServletRequest request) throws Exception {

		SessionUserDto user = (SessionUserDto) httpSession.getAttribute("user");

		if(user != null){
			model.addAttribute("username", user.getUsername());
			model.addAttribute("useremail", user.getEmail());

			String role = user.getRole();
			if(role.equals("ROLE_ADMIN")) {
			    model.addAttribute("admin", true);
			} else if (role.equals("ROLE_MANAGER")) {
				model.addAttribute("manager", true);
			}

			logger.info(user.getUsername() + "reached" + ": \"/\"");
		} else {
			// log
			logger.info("reached" + ": \"/\"");
		}



		return "mypage";
	}


	@GetMapping("/users/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);

		List<Account> listUsers = accountService.listAll();

		UserPDFExporter exporter = new UserPDFExporter(listUsers);
		exporter.export(response);

	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public String list(Model model) {

		List<Account> users = accountService.getUsers();
		model.addAttribute("users", users);
		return "members/userList";
	}


}
