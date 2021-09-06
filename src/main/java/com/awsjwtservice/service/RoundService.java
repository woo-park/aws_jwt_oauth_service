package com.awsjwtservice.service;


import com.awsjwtservice.domain.*;
import com.awsjwtservice.domain.item.Item;
import com.awsjwtservice.dto.HolesDto;
import com.awsjwtservice.repository.HoleRepository;
import com.awsjwtservice.repository.RoundRepository;
import com.awsjwtservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class RoundService {

    @Autowired
    UserRepository memberRepository;

    @Autowired
    RoundRepository roundRepository;


    @Autowired
    HoleRepository holeRepository;

    @Autowired
    HoleService holeService;

    public List<Rounds> findAllRounds(long accountId) {

        return (List<Rounds>) roundRepository.findAll(accountId);
    }

    public Holes checkHoleExistsAndCreate(Rounds round, HolesDto holesDto) {
        List<Holes> holes = round.getHoles();


        // create new hole
        int bunker = holesDto.getBunker().equals("O") ? 1 : 0;
        int upDown = holesDto.getUpDown().contentEquals("O") ? 1 : 0;
        int fairway = holesDto.getFairway().equalsIgnoreCase("O") ? 1 : 0;
        int onGreen = holesDto.getOnGreen().equals("O") ? 1 : 0;

        for(Holes hole : holes) {
            if(hole.getHoleNumber() == holesDto.getHoleNumber()) {

                hole.setBunker(bunker);
                hole.setUpDown(upDown);
                hole.setFairway(fairway);
                hole.setOnGreen(onGreen);
                hole.setScore(holesDto.getScore());
                hole.setPar(holesDto.getPar());
                hole.setPutt(holesDto.getPutt());

//                System.out.println(hole.getHoleNumber());
//                System.out.println("hole already existed -> hence we mutated and now saving");
                // update hole
                holeService.saveHole(hole);
                return hole;
            }
        }


        Holes newHole = Holes.createHoleInformation(round, holesDto.getScore(),holesDto.getPar(),bunker,holesDto.getPutt(),upDown,fairway, onGreen, holesDto.getHoleNumber());

//        System.out.println(newHole.getHoleNumber());
//        System.out.println("hole didn't exist -> hence we build & now saving");
        holeService.saveHole(newHole);

        return newHole;

    }

    public Rounds findRound(long roundId) {
        return roundRepository.findOne(roundId);
    }

    /** 주문 */
    public Long createRound(Account member, String courseName) {

        List<Holes> holes = new ArrayList<>();
//
        Rounds round = Rounds.createRound(member, courseName, holes);
//
//        for(int i = 0; i < 18; i++) {
//            holes.add(Hole.createHoleInformation(round, 0));
//        }
//
        roundRepository.save(round);

        return round.getId();
    }


    public void updateRound(Rounds round) {
        roundRepository.save(round);
    }

    public Rounds findLatestRound(long accountId) {
        return roundRepository.findLatestRound(accountId);
    }
}
