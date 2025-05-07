package org.kosa.sbb.question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.kosa.sbb.DataNotFountException;
import org.kosa.sbb.answer.Answer;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
	private final QuestionRepository questionRepository;

	public Question getQuestion(Integer id) {
		Optional<Question> oq = questionRepository.findById(id);
		if (oq.isPresent()) {
			return oq.get();
		}
		else {
			throw new DataNotFountException("question nt found");
		}
	}

	public List<Question> findAll() {
		return questionRepository.findAll();
	}
	
	public void create(String subject, String content) {
		Question question = new Question();
		question.setSubject(subject);
		question.setContent(content);
		question.setCreateDate(LocalDateTime.now());
		
		questionRepository.save(question);
	}	
}
