package org.kosa.sbb.answer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.kosa.sbb.DataNotFountException;
import org.kosa.sbb.question.Question;
import org.kosa.sbb.user.SiteUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerService {
	private final AnswerRepository answerRepository;

	public void create(Question question, String content, String ip, SiteUser siteUser) {
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setIp(ip);
		answer.setCreateDate(LocalDateTime.now());
		answer.setQuestion(question);
		answer.setAuthor(siteUser); //작성자 정보인 아이디 설정 
		
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

	public Answer getAnswer(Integer id) {
		return answerRepository.findById(id)
				.orElseThrow(() -> new DataNotFountException("answer not found"));
	}

	public void modify(Answer answer, String content) {
		answer.setContent(content);
		answer.setModifyDate(LocalDateTime.now());
		this.answerRepository.save(answer);
	}

	public void delete(Answer answer) {
		this.answerRepository.delete(answer);
	}

	public void vote(Answer answer, SiteUser siteUser) {
		if (answer.getVoter().contains(siteUser)) {
			//추천인 삭제 
			answer.getVoter().remove(siteUser);
		} else {
			//추천인 추가  
			answer.getVoter().add(siteUser);
		}
		answerRepository.save(answer);		
	}

}

