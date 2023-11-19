package com.smart.watchboard.service;

import com.smart.watchboard.domain.Answer;
import com.smart.watchboard.dto.AnswerDto;
import com.smart.watchboard.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {
    private final AnswerRepository answerRepository;

    public void createAnswer(Long documentId, String keyword, ResponseEntity<AnswerDto> responseEntity) {
        String text = responseEntity.getBody().getText();
        if (text.equals("init")) {
            Answer answer = Answer.builder()
                    .documentId(documentId)
                    .keyword(keyword)
                    .text("processing")
                    .build();
            answerRepository.save(answer);
        } else {
            AnswerDto answerDto = getAnswer(documentId, keyword);
            if (answerDto.getText().equals("processing")) {
                Answer answer = getAnswerForCreate(documentId, keyword);
                answer.setText(responseEntity.getBody().getText());
                answerRepository.save(answer);
            }
        }
    }

    public Answer getAnswerForCreate(Long documentId, String keyword) {
        Optional<Answer> answer = answerRepository.findByDocumentIdAndKeyword(documentId, keyword);
        Answer answerData = answer.orElse(null);
        return answerData;
    }
    public AnswerDto getAnswer(Long documentId, String keyword) {
        Optional<Answer> answer = answerRepository.findByDocumentIdAndKeyword(documentId, keyword);
        if (answer.isEmpty()) {
            return null;
        }
        AnswerDto answerDto = new AnswerDto(answer.get().getText());
        return answerDto;
    }
}
