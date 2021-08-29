package com.awsjwtservice.service;


import com.awsjwtservice.domain.*;
import com.awsjwtservice.domain.item.Item;
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

    public List<Rounds> findAllRounds(long accountId) {

        return (List<Rounds>) roundRepository.findAll(accountId);
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


    public void saveHole(Holes hole) {
        holeRepository.save(hole);
    }

    public void updateRound(Rounds round) {
        roundRepository.save(round);
    }
}
