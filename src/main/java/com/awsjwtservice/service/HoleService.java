package com.awsjwtservice.service;


import com.awsjwtservice.domain.Holes;
import com.awsjwtservice.repository.HoleRepository;
import com.awsjwtservice.repository.RoundRepository;
import com.awsjwtservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HoleService {

    @Autowired
    UserRepository memberRepository;

    @Autowired
    RoundRepository roundRepository;


    @Autowired
    HoleRepository holeRepository;


    public void saveHole(Holes hole) {
        holeRepository.save(hole);
    }
}
