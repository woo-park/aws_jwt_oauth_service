package com.awsjwtservice.controller;

import com.awsjwtservice.domain.Account;
import com.awsjwtservice.domain.item.Item;
import com.awsjwtservice.dto.SessionUserDto;
import com.awsjwtservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ScoreController {

    @Autowired
    AccountService userService;

    @Autowired
    AccountService accountService;

    // get a logger
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserController.class);

    private final HttpSession httpSession;

    public ScoreController(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    /* get Score Card */
    @RequestMapping(value = "/score", method = RequestMethod.GET)
    public String getScoreCard(Model model) {

        SessionUserDto user = (SessionUserDto) httpSession.getAttribute("user");

        if (user != null) {
            Account account = accountService.findUser(user.getEmail());

            try {
                model.addAttribute("user", account);

                // 모든 필드 history 리스트를 보여줘야하나?

            } catch (Exception e) {
                logger.info("can't find user");
            }

            return "score";
        } else {
            return "redirect:/oauth_login";
        }


    }

}
