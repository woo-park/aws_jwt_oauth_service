package com.awsjwtservice.repository;

import com.awsjwtservice.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT p FROM Question p ORDER BY p.id DESC")
    List<Question> findAllDesc();


}