package org.kosa.sbb.answer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
	List<Answer> findByQuestionIdAndIdGreaterThan(Integer questionId, Integer id);
}
