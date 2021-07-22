package com.awsjwtservice.controller;

import com.awsjwtservice.domain.Account;
import com.awsjwtservice.dto.AccountDto;
import com.awsjwtservice.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    // get a logger
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserController.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountService accountService;

    @GetMapping("/register")                                   // hmmm interesting approach: using term 'users' as registering page, and also as a receiving portal for post method
    public String createUser(Model model) {
        logger.info("/register reached");
        return "register";
    }

    @PostMapping("/register")
    public String createUser(AccountDto accountDto) {
        //ModelMapper api 를 사용한다, 그러면 dto를 바로 model로 map할수있다
//        ModelMapper modelMapper = new ModelMapper();
//        Account account = modelMapper.map(accountDto, Account.class);
//
//        account.setPassword(passwordEncoder.encode(account.getPassword()));


        accountService.createUserIfNotFound(accountDto);
//        accountService.createUser(account);

        // log
        logger.info("registered: " + accountDto.getEmail());


        return "redirect:/mypage";
    }

}
