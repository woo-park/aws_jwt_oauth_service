package com.awsjwtservice.controller;


import com.awsjwtservice.domain.Account;
import com.awsjwtservice.domain.Rounds;
import com.awsjwtservice.dto.RoundsDto;
import com.awsjwtservice.dto.SessionUserDto;
import com.awsjwtservice.service.AccountService;
import com.awsjwtservice.service.RoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class RoundController {

    @Autowired
    AccountService userService;

    @Autowired
    AccountService accountService;

    @Autowired
    RoundService roundService;

    // get a logger
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserController.class);

    private final HttpSession httpSession;

    public RoundController(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    @RequestMapping(value = "/rounds", method = RequestMethod.POST)
    public String createRound(RoundsDto roundDetails, Model model) {

        SessionUserDto user = (SessionUserDto) httpSession.getAttribute("user");

        if (user != null) {
            Account account = accountService.findUser(user.getEmail());

            try {
                model.addAttribute("user", account);

                long roundId = roundService.createRound(account, roundDetails.getCourseName());
                String redirectString = "redirect:/rounds/" + roundId;

                return redirectString;

            } catch (Exception e) {
                logger.info("can't find user");
                return "redirect:/oauth_login";
            }

        } else {
            return "redirect:/oauth_login";
        }


    }


    /* get Score Card */
    @RequestMapping(value = "/rounds", method = RequestMethod.GET)
    public String rounds(Model model) {

        SessionUserDto user = (SessionUserDto) httpSession.getAttribute("user");

        if (user != null) {
            Account account = accountService.findUser(user.getEmail());

            try {
                model.addAttribute("user", account);

            } catch (Exception e) {
                logger.info("can't find user");
            }


            // a portal that has access to holes 1 ~ 18

            return "round";
        } else {
            return "redirect:/oauth_login";
        }


    }

    /* get Score Card */
    @RequestMapping(value = "/rounds/{roundId}", method = RequestMethod.GET)
    public String getRound(@PathVariable("roundId") long roundId, Model model) {

        SessionUserDto user = (SessionUserDto) httpSession.getAttribute("user");

        if (user != null) {
            Account account = accountService.findUser(user.getEmail());

            try {
                model.addAttribute("user", account);

                Rounds round = roundService.findRound(roundId);

                model.addAttribute("round", round);

            } catch (Exception e) {
                logger.info("can't find user");
            }


            // a portal that has access to holes 1 ~ 18

            return "round";
        } else {
            return "redirect:/oauth_login";
        }


    }
}
