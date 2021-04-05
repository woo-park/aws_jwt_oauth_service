package com.awsjwtservice.controller;


import com.awsjwtservice.config.annotation.LoginUser;
import com.awsjwtservice.dto.SessionUserDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class QuestionController {


    @GetMapping("/question/create")
    public String createSite(Model model, @LoginUser SessionUserDto user) throws Exception {
        if(user != null) {
            model.addAttribute("userSeq", user.getUserSeq());
            model.addAttribute("email", user.getEmail());
        }

        return "createQuestion";
    }

}
