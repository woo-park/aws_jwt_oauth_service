package com.awsjwtservice.controller;


import com.awsjwtservice.domain.*;
import com.awsjwtservice.domain.item.Book;
import com.awsjwtservice.dto.HolesDto;
import com.awsjwtservice.dto.RoundsDto;
import com.awsjwtservice.dto.SessionUserDto;
import com.awsjwtservice.repository.HoleRepository;
import com.awsjwtservice.repository.RoundRepository;
import com.awsjwtservice.service.AccountService;
import com.awsjwtservice.service.HoleService;
import com.awsjwtservice.service.RoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RoundController {

    @Autowired
    AccountService userService;

    @Autowired
    AccountService accountService;

    @Autowired
    RoundService roundService;

    @Autowired
    HoleService holeService;

    @Autowired
    RoundRepository roundRepository;

    @Autowired
    HoleRepository holeRepository;

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

                List<Rounds> rounds = roundService.findAllRounds(account.getId());

                List<RoundsDto> roundsDtos = new ArrayList<>();

                int counter = 0;
                for (Rounds round : rounds) {
                    roundsDtos.add(
                            RoundsDto.builder()
                                    .courseName(round.getCourseName())
                                    .roundDate(round.getRoundDate())
                                    .index(counter += 1)
                                    .roundId(round.getId())
                                    .build()
                    );
                }


                model.addAttribute("rounds", roundsDtos);

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

                RoundsDto roundsDto = RoundsDto.builder()
                                        .courseName(round.getCourseName())
                                        .roundDate(round.getRoundDate())
                                        .roundId(round.getId())
                                        .build();

                List<HolesDto> holesDto = new ArrayList<>();

                for(int i = 1; i <= 18; i++) {
                    holesDto.add(HolesDto.builder()
                            .par(0)
                            .roundId(roundId)
                            .putt(0)
                            .bunker("")
                            .upDown("")
                            .fairway("")
                            .onGreen("")
                            .score(0)
                            .holeNumber(i)
                            .build());
                }

                for(Holes hole : holes) {
                    int j = hole.getHoleNumber();
                    holesDto.set(j - 1, HolesDto.builder()
                            .par(hole.getPar())
                            .roundId(roundId)
                            .putt(hole.getPutt())
                            .bunker(hole.getBunker() == 1 ? "O" : "X")
                            .upDown(hole.getUpDown() == 1 ? "O" : "X")
                            .fairway(hole.getFairway() == 1 ? "O" : "X")
                            .onGreen(hole.getOnGreen() == 1 ? "O" : "X")
                            .score(hole.getScore())
                            .holeNumber(hole.getHoleNumber())
                            .build());

                }

                model.addAttribute("roundsDto", roundsDto);
                model.addAttribute("holesDto", holesDto);

            } catch (Exception e) {
                logger.info("error occured during finding rounds & holes");
            }


            // a portal that has access to holes 1 ~ 18

            return "editRound";
//        } else {
//            return "redirect:/oauth_login";
//        }


    }


    /* HolesDto holesDto,  로 받는 작업 해야함 */
    @RequestMapping(value = "/rounds/{roundId}/{holeNumber}", method = RequestMethod.POST)
    public String updateRound(@PathVariable("roundId") long roundId, @PathVariable("holeNumber") int holeNumber, @RequestBody HolesDto holesDto, Model model) {
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
                } else {
                    Holes hole = roundService.checkHoleExistsAndCreate(round, holesDto);

                    // check round hole list and append if it doesn't exists
                    round.updateHoles(hole);


                    // instead of saving it here, we moved inside function checkHoleExistsAndCreate
                    // holeRepository.save(hole);

                    roundService.updateRound(round);

                    model.addAttribute("roundId", roundId);
                    model.addAttribute("holeNumber", holeNumber);

                    model.addAttribute("hole", hole);

                    return "hole";
                }

            } catch (Exception e) {
                logger.info("error occured during finding rounds & holes");
            }


            // a portal that has access to holes 1 ~ 18

            return "editRound";
        } else {
            return "redirect:/oauth_login";
        }
    }

        /* get Score Card */
//    @RequestMapping(value = "/rounds/{roundId}/{holeNumber}", method = RequestMethod.GET)
    @GetMapping("/rounds/{roundId}/{holeNumber}")
    public String getRound(@PathVariable("roundId") long roundId,@PathVariable("holeNumber") int holeNumber, Model model) throws Exception {

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
                } else {


                    Holes hole = holeRepository.findByHoleNumber(holeNumber);

                    if( hole != null) {
                        HolesDto holesDto = HolesDto.builder()
                                .par(hole.getPar())
//                                    .updatedDate()
                                .roundId(roundId)
                                .putt(hole.getPutt())
                                .bunker(hole.getBunker() == 1 ? "O" : "X")
                                .fairway(hole.getFairway() == 1 ? "O" : "X")
                                .upDown(hole.getUpDown() == 1 ? "O" : "X")
                                .onGreen(hole.getOnGreen() == 1 ? "O" : "X")
                                .score(hole.getScore())
                                .holeNumber(hole.getHoleNumber())
                                .build();

                        model.addAttribute("holesDto", holesDto);
                    } else {

                    }
                    // figure out when it doesn't exists <-- !important



                    /*
                    List<Holes> holes = round.getHoles();

                    for(Holes hole : holes) {
                        if(hole.getHoleNumber() == holeNumber) {

                            HolesDto holesDto = HolesDto.builder()
                                    .par(hole.getPar())
//                                    .updatedDate()
                                    .roundId(roundId)
                                    .putt(hole.getPutt())
                                    .bunker(hole.getBunker() == 1 ? "O" : "X")
                                    .fairway(hole.getFairway() == 1 ? "O" : "X")
                                    .upDown(hole.getUpDown() == 1 ? "O" : "X")
                                    .onGreen(hole.getOnGreen() == 1 ? "O" : "X")
                                    .holeNumber(hole.getHoleNumber())
                                    .build();

                            model.addAttribute("holesDto", holesDto);
                            break;
                        }
                    }
                    */

//                    model.addAttribute("roundId", roundId);
//                    model.addAttribute("holeNumber", holeNumber);
//
//                    model.addAttribute("round", holes.get(0));
//
//                    model.addAttribute("holes", holes);




                    return "hole";
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
            return "editRound";
//            return "redirect:/oauth_login";
        }


    }
}