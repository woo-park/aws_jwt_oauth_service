package com.awsjwtservice.service;


import com.awsjwtservice.domain.Question;
import com.awsjwtservice.dto.QuestionListResponseDto;
import com.awsjwtservice.dto.QuestionResponseDto;
import com.awsjwtservice.dto.QuestionSaveRequestDto;
import com.awsjwtservice.dto.QuestionUpdateRequestDto;
import com.awsjwtservice.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    @Transactional
    public Long save(QuestionSaveRequestDto requestDto) {
        return questionRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, QuestionUpdateRequestDto requestDto) {

        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));

        question.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    @Transactional
    public void delete (Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));

        questionRepository.delete(question);
    }

//    @Transactional(readOnly = true)
    public QuestionResponseDto findById(Long id) {
        Question entity = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));

        return new QuestionResponseDto(entity);
    }

//    @Transactional(readOnly = true)
    public List<QuestionListResponseDto> findAllDesc() {
        return questionRepository.findAllDesc().stream()
                .map(QuestionListResponseDto::new)
                .collect(Collectors.toList());
    }
}