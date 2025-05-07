package org.kosa.sbb.answer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.kosa.sbb.DataNotFountException;
import org.kosa.sbb.question.Question;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerService {
	private final AnswerRepository answerRepository;

	public void create(Question question, String content, String ip) {
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setIp(ip);
		answer.setCreateDate(LocalDateTime.now());
		answer.setQuestion(question);
		
		answerRepository.save(answer);
	}

	@Transactional
	public List<AnswerDTO> findByQuestionIdAndIdGreaterThan(Integer questionId, Integer id) {
		return answerRepository.findByQuestionIdAndIdGreaterThan(questionId, id).stream()
			    .map(answer -> AnswerDTO.of(answer)).toList();
//		List<AnswerDTO> result = new ArrayList<AnswerDTO>();
//		for (var answer : answerRepository.findByQuestionIdAndIdGreaterThan(questionId, id)) {
//			result.add(AnswerDTO.of(answer));
//		}
//		return result; 
	}

}

