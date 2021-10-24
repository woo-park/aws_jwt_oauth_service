package com.awsjwtservice.service;


import com.awsjwtservice.domain.*;
import com.awsjwtservice.domain.item.Item;
import com.awsjwtservice.dto.HolesDto;
import com.awsjwtservice.repository.HoleRepository;
import com.awsjwtservice.repository.RoundRepository;
import com.awsjwtservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import org.springframework.data.domain.Pageable;
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

    public Page<Rounds> findAllRounds(Pageable pageable) {
        String privacyStatus = "public";

        return (Page<Rounds>) roundRepository.findAll(privacyStatus, pageable);
    }

    public List<Rounds> findAllRounds(long accountId) {

        return (List<Rounds>) roundRepository.findAll(accountId);
    }

    public Integer convertStringToInt(String str) {
        Integer integerFormatted = null;

        if(str.equals("")) {
            integerFormatted = null;
        } else if(str.equals("O")) {
            integerFormatted = 1;
        } else if(str.equals("X")) {
            integerFormatted = 0;
        }

        return integerFormatted;
    }

    public Integer validateIntegerInput(Integer arg) {
        Integer integerFormatted = null;

        if(arg == null) {
//            integerFormatted = null;
        } else {
            integerFormatted = arg;
        }

        return integerFormatted;
    }

    public Holes checkHoleExistsAndCreate(Rounds round, HolesDto holesDto) {
        List<Holes> holes = round.getHoles();

        for(Holes hole : holes) {
            if(hole.getHoleNumber() == holesDto.getHoleNumber()) {

                hole.setBunker(convertStringToInt(holesDto.getBunker()));
                hole.setUpDown(convertStringToInt(holesDto.getUpDown()));
                hole.setFairway(convertStringToInt(holesDto.getFairway()));
                hole.setOnGreen(convertStringToInt(holesDto.getOnGreen()));
                hole.setScore(validateIntegerInput(holesDto.getScore()));
                hole.setPar(validateIntegerInput(holesDto.getPar()));
                hole.setPutt(validateIntegerInput(holesDto.getPutt()));

                System.out.println(hole.getHoleNumber());
                System.out.println("hole already existed -> hence we mutated and now saving");
                // update hole
                holeService.saveHole(hole);
                return hole;
            }
        }

        Holes newHole = Holes.createHoleInformation(round,
                                                validateIntegerInput(holesDto.getScore()),
                                                validateIntegerInput(holesDto.getPar()),
                                                convertStringToInt(holesDto.getBunker()),
                                                validateIntegerInput(holesDto.getPutt()),
                                                convertStringToInt(holesDto.getUpDown()),
                                                convertStringToInt(holesDto.getFairway()),
                                                convertStringToInt(holesDto.getOnGreen()),
                                                holesDto.getHoleNumber());

        System.out.println(newHole.getHoleNumber());
        System.out.println("hole didn't exist -> hence we build & now saving");

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
        System.out.println("updateRound");
        roundRepository.save(round);
    }

    public Rounds findLatestRound(long accountId) {
        return roundRepository.findLatestRound(accountId);
    }
}
