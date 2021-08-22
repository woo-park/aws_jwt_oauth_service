package com.awsjwtservice.controller;


import com.awsjwtservice.domain.Account;
import com.awsjwtservice.domain.Holes;
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
import java.util.List;

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

//        SessionUserDto user = (SessionUserDto) httpSession.getAttribute("user");
//
//        if (user != null) {
//            Account account = accountService.findUser(user.getEmail());

            try {
//                model.addAttribute("user", account);

                Rounds round = roundService.findRound(roundId);
                List<Holes> holes = round.getHoles();
                String courseName = round.getCourseName();
                System.out.println(holes);
                model.addAttribute("courseName", courseName);
                model.addAttribute("round", round);
                model.addAttribute("holes", holes);

            } catch (Exception e) {
                logger.info("error occured during finding rounds & holes");
            }


            // a portal that has access to holes 1 ~ 18

            return "editRound";
//        } else {
//            return "redirect:/oauth_login";
//        }


    }


    /* get Score Card */
    @RequestMapping(value = "/rounds/{roundId}/{holeNumber}", method = RequestMethod.GET)
    public String editRound(@PathVariable("roundId") long roundId,@PathVariable("holeNumber") int holeNumber, Model model) {

        SessionUserDto user = (SessionUserDto) httpSession.getAttribute("user");

        if (user != null) {
            Account account = accountService.findUser(user.getEmail());

            try {
    //                model.addAttribute("user", account);

                Rounds round = roundService.findRound(roundId);

                if(round.getAccount().getId() != account.getId()) {

                    logger.info("not the owner of this round.");
                    String redirectString = "redirect:/rounds/" + roundId + "?msg=no_access";
                    return redirectString;
                }

//                List<Holes> holes = round.getHoles();
//                String courseName = round.getCourseName();
//                System.out.println(holes);
//                model.addAttribute("courseName", courseName);
//                model.addAttribute("round", round);
//                model.addAttribute("holes", holes);

            } catch (Exception e) {
                logger.info("error occured during finding rounds & holes");
            }


            // a portal that has access to holes 1 ~ 18

            return "editRound";
        } else {
            return "redirect:/oauth_login";
        }


    }
}
