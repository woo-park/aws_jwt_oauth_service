package com.awsjwtservice.controller;

import com.awsjwtservice.config.annotation.LoginUser;
import com.awsjwtservice.dto.*;
import com.awsjwtservice.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class QuestionRestController {

    private final QuestionService questionService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody QuestionSaveRequestDto requestDto) {
        return questionService.save(requestDto);
    }

    @PostMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody QuestionUpdateRequestDto requestDto) {
        return questionService.update(id, requestDto);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id) {
        questionService.delete(id);
        return id;
    }

    @GetMapping("/api/v1/posts/{id}")
    public QuestionResponseDto findById(@PathVariable Long id) {
        return questionService.findById(id);
    }

    @GetMapping("/api/v1/posts/list")
    public List<QuestionListResponseDto> findAll() {
        return questionService.findAllDesc();
    }
}
