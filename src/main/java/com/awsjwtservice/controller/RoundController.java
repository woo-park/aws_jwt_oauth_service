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

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
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

    public String convertToString(Integer arg) {
        String formatted = "";

        if(arg == null) {
            formatted = "";
        } else if (arg == 1) {
            formatted = "O";
        } else if (arg == 0) {
            formatted = "X";
        }
        return formatted;
    }


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
                model.addAttribute("name", account.getUsername());
                model.addAttribute("email", account.getEmail());
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
    @RequestMapping(value = "/rounds/start", method = RequestMethod.POST)
    public String startRound(RoundsDto roundDetails, Model model) {
        SessionUserDto user = (SessionUserDto) httpSession.getAttribute("user");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (user != null) {
            Account account = accountService.findUser(user.getEmail());

            try {
                model.addAttribute("user", account);



                long roundId = roundService.createRound(account, roundDetails.getCourseName());
                String redirectString = "redirect:/rounds/" + roundId;

                return redirectString;

            } catch (Exception e) {
                logger.info("can't find user");
            }
            // a portal that has access to holes 1 ~ 18

            return "startRound";
        } else {
            return "redirect:/oauth_login";
        }
    }



    /* get Score Card */
    @RequestMapping(value = "/rounds/start", method = RequestMethod.GET)
    public String getStartRound(Model model) {
        SessionUserDto user = (SessionUserDto) httpSession.getAttribute("user");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (user != null) {
            Account account = accountService.findUser(user.getEmail());

            try {

                model.addAttribute("user", account);
                model.addAttribute("name", account.getUsername());
                model.addAttribute("email", account.getEmail());

            } catch (Exception e) {
                logger.info("can't find user");
            }
            // a portal that has access to holes 1 ~ 18

            return "startRound";
        } else {
            return "redirect:/oauth_login";
        }
    }


    /* get Score Card */
    @RequestMapping(value = "/rounds", method = RequestMethod.GET)
    public String rounds(Model model) {

        SessionUserDto user = (SessionUserDto) httpSession.getAttribute("user");


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");


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
                                    .formattedDateTime(round.getRoundDate() != null ? round.getRoundDate().format(formatter) : "")
                                    .formattedDate(round.getRoundDate() != null ? round.getRoundDate().toLocalDate().format(dateFormatter) : "")
                                    .formattedTime(round.getRoundDate() != null ? round.getRoundDate().toLocalTime().format(timeFormatter) : "")
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

            return "roundList";
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
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");


                Integer totalScore = 0;
                /* Putts Per Round “GIR” */
                Integer puttsPerRoundGIR = 0;
                Integer greenInRegulationNumber = 0;

                Rounds round = roundService.findRound(roundId);
                List<Holes> holes = round.getHoles();

                RoundsDto roundsDto = RoundsDto.builder()
                                        .courseName(round.getCourseName())
                                        .formattedDate(round.getRoundDate() != null ? round.getRoundDate().toLocalDate().format(dateFormatter) : "")
                                        .formattedTime(round.getRoundDate() != null ? round.getRoundDate().toLocalTime().format(timeFormatter) : "")
                                        .roundDate(round.getRoundDate())
                                        .roundId(round.getId())
                                        .build();

                List<HolesDto> holesDto = new ArrayList<>();

                for(int i = 1; i <= 18; i++) {
                    holesDto.add(HolesDto.builder()
                            .par(null)
                            .roundId(roundId)
                            .putt(null)
                            .bunker("")
                            .upDown("")
                            .fairway("")
                            .onGreen("")
                            .score(null)
                            .holeNumber(i)
                            .build());
                }

                for(Holes hole : holes) {
                    int j = hole.getHoleNumber();

                    Boolean greenInRegulation = false;

                    if(hole.getPar() != null && hole.getScore() != null) {
                        int holeScore = hole.getPar() + hole.getScore();

                        totalScore += holeScore;
                    }

                    if(hole.getPutt() != null && hole.getOnGreen() != null && hole.getOnGreen() != 0) {
                        greenInRegulation = true;
                        greenInRegulationNumber += 1;
//                        System.out.println("hole#" + hole.getHoleNumber() + "in regulation");
                        puttsPerRoundGIR += hole.getPutt();
                    }

                    holesDto.set(j - 1, HolesDto.builder()
                            .par(hole.getPar())
                            .roundId(roundId)
                            .putt(hole.getPutt())
                            .bunker(convertToString(hole.getBunker()))
                            .upDown(convertToString(hole.getUpDown()))
                            .fairway(convertToString(hole.getFairway()))
                            .onGreen(convertToString(hole.getOnGreen()))
                            .score(hole.getScore())
                            .holeNumber(hole.getHoleNumber())
                            .build());

                }



                /*
                    Basic stats calculation
                */
                model.addAttribute("totalScore", totalScore);
                if(greenInRegulationNumber != 0) {
                    model.addAttribute("puttsPerRoundGIR", (String.format("%.2f", (float)puttsPerRoundGIR / greenInRegulationNumber)));
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
    public void updateRound(@PathVariable("roundId") long roundId, @PathVariable("holeNumber") int holeNumber, @RequestBody HolesDto holesDto, Model model, HttpServletResponse response) throws IOException {
        SessionUserDto user = (SessionUserDto) httpSession.getAttribute("user");

        if (user != null) {
            Account account = accountService.findUser(user.getEmail());

            try {
                //                model.addAttribute("user", account);

                Rounds round = roundService.findRound(roundId);

                if(round.getAccount().getId() != account.getId()) {

                    logger.info("not the owner of this round.");
//                    String redirectString = "redirect:/rounds/" + roundId + "?msg=no_access";
                    String redirectString = "/rounds/" + roundId + "?msg=no_access";
//                    return redirectString;
                    response.sendRedirect(redirectString);
                } else {
                    Holes hole = roundService.checkHoleExistsAndCreate(round, holesDto);

                    // check round hole list and append if it doesn't exists
                    round.updateHoles(hole);


                    // instead of saving it here, we moved inside function checkHoleExistsAndCreate
                    // holeRepository.save(hole);

                    roundService.updateRound(round);

//                    if( hole != null) {
//                        HolesDto dto = HolesDto.builder()
//                                .par(hole.getPar())
////                                    .updatedDate()
//                                .roundId(roundId)
//                                .putt(hole.getPutt())
//                                .bunker(convertToString(hole.getBunker()))
//                                .upDown(convertToString(hole.getUpDown()))
//                                .fairway(convertToString(hole.getFairway()))
//                                .onGreen(convertToString(hole.getOnGreen()))
//                                .score(hole.getScore())
//                                .holeNumber(hole.getHoleNumber())
//                                .build();
//
//                        model.addAttribute("holesDto", dto);
//                        model.addAttribute("roundId", roundId);
//                    } else {
//
//                        HolesDto dto = HolesDto.builder()
//                                .holeNumber(holeNumber)
//                                .build();
//                        model.addAttribute("holesDto", dto);
//                        model.addAttribute("roundId", roundId);
//                    }
//
//                    return "scoreHole";
                    String redirectString = "/rounds/" + roundId;
                    response.sendRedirect(redirectString);
                }

            } catch (Exception e) {
                logger.info("error occured during finding rounds & holes");
                System.out.println("error occured during finding rounds & holes");
                String redirectString = "/rounds/" + roundId + "?msg=no_round_found";
                response.sendRedirect(redirectString);
            }


//            response.sendRedirect("/rounds");


            // a portal that has access to holes 1 ~ 18

//            return "scoreHole"; //testing

        } else {
//            return "redirect:/oauth_login";

            response.sendRedirect("/oauth_login");
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

                Rounds round = roundService.findRound(roundId);

                if(round.getAccount().getId() != account.getId()) {

                    logger.info("not the owner of this round.");
                    String redirectString = "redirect:/rounds/" + roundId + "?msg=no_access";
                    return redirectString;
                } else {


                    Holes hole = holeRepository.findByHoleNumberAndRound(holeNumber, round);


                    if( hole != null) {
                        HolesDto holesDto = HolesDto.builder()
                                .par(hole.getPar())
//                                    .updatedDate()
                                .roundId(roundId)
                                .putt(hole.getPutt())
                                .bunker(convertToString(hole.getBunker()))
                                .upDown(convertToString(hole.getUpDown()))
                                .fairway(convertToString(hole.getFairway()))
                                .onGreen(convertToString(hole.getOnGreen()))
                                .score(hole.getScore())
                                .holeNumber(hole.getHoleNumber())
                                .build();

                        if(holeNumber > 1 && holeNumber < 18) {
                            model.addAttribute("nextHole", holeNumber + 1);
                            model.addAttribute("previousHole", holeNumber - 1);
                        } else if (holeNumber == 1) {
                            model.addAttribute("nextHole", holeNumber + 1);
                        } else if (holeNumber == 18) {
                            model.addAttribute("previousHole", holeNumber - 1);
                        }

                        model.addAttribute("holesDto", holesDto);
                        model.addAttribute("roundId", roundId);
                    } else {

                        if(holeNumber > 1 && holeNumber < 18) {
                            model.addAttribute("nextHole", holeNumber + 1);
                            model.addAttribute("previousHole", holeNumber - 1);
                        } else if (holeNumber == 1) {
                            model.addAttribute("nextHole", holeNumber + 1);
                        } else if (holeNumber == 18) {
                            model.addAttribute("previousHole", holeNumber - 1);
                        }

                        HolesDto holesDto = HolesDto.builder()
                                .holeNumber(holeNumber)
                                .build();
                        model.addAttribute("holesDto", holesDto);
                        model.addAttribute("roundId", roundId);
                    }



                    return "scoreHole";
                }


            } catch (Exception e) {
                logger.info("error occured during finding rounds & holes");
            }



            // a portal that has access to holes 1 ~ 18

            return "scoreHole"; //testing
        } else {
            return "scoreHole";
//            return "redirect:/oauth_login";
        }


    }


    /*
    * latest round
    * */

    @RequestMapping(value = "/rounds/latest", method = RequestMethod.GET)
    public String getLatestRound(Model model) {

        SessionUserDto user = (SessionUserDto) httpSession.getAttribute("user");



        if (user != null) {
            Account account = accountService.findUser(user.getEmail());

            try {

                /* Putts Per Round “GIR” */
                Integer puttsPerRoundGIR = 0;
                Integer greenInRegulationNumber = 0;

                Rounds round = roundService.findLatestRound(account.getId());

                if(round != null) {
                    Integer totalScore = 0;

                    RoundsDto roundsDto = RoundsDto.builder()
                            .courseName(round.getCourseName())
                            .roundDate(round.getRoundDate())
                            .roundId(round.getId())
                            .build();


                    List<Holes> holes = round.getHoles();


                    List<HolesDto> holesDto = new ArrayList<>();

                    for(int i = 1; i <= 18; i++) {
                        holesDto.add(HolesDto.builder()
                                .par(null)
                                .roundId(roundsDto.getRoundId())
                                .putt(null)
                                .bunker("")
                                .upDown("")
                                .fairway("")
                                .onGreen("")
                                .score(null)
                                .holeNumber(i)
                                .build());
                    }

                    if(holes.size() != 0) {
                        for(Holes hole : holes) {
                            int j = hole.getHoleNumber();

                            Boolean greenInRegulation = false;

                            if(hole.getPar() != null && hole.getScore() != null) {
                                int holeScore = hole.getPar() + hole.getScore();

                                totalScore += holeScore;
                            }

                            if(hole.getPutt() != null && hole.getOnGreen() != null && hole.getOnGreen() != 0) {
                                greenInRegulation = true;
                                greenInRegulationNumber += 1;
//                        System.out.println("hole#" + hole.getHoleNumber() + "in regulation");
                                puttsPerRoundGIR += hole.getPutt();
                            }

                            holesDto.set(j - 1, HolesDto.builder()
                                    .par(hole.getPar())
                                    .roundId(roundsDto.getRoundId())
                                    .putt(hole.getPutt())
                                    .bunker(convertToString(hole.getBunker()))
                                    .upDown(convertToString(hole.getUpDown()))
                                    .fairway(convertToString(hole.getFairway()))
                                    .onGreen(convertToString(hole.getOnGreen()))
                                    .score(hole.getScore())
                                    .holeNumber(hole.getHoleNumber())
                                    .build());

                        }
                    } else {

                    }


                    /*
                        Basic stats calculation
                    */
                    model.addAttribute("totalScore", totalScore);
                    if(greenInRegulationNumber != 0) {
                        model.addAttribute("puttsPerRoundGIR", (String.format("%.2f", (float)puttsPerRoundGIR / greenInRegulationNumber)));
                    }


                    model.addAttribute("roundsDto", roundsDto);
                    model.addAttribute("holesDto", holesDto);
                }


            } catch (Exception e) {
                logger.info("error occured during finding rounds & holes");
            }

            return "editRound";
        } else {
            return "redirect:/oauth_login";
        }
    }

}
