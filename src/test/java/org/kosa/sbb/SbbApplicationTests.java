package org.kosa.sbb;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.kosa.sbb.answer.Answer;
import org.kosa.sbb.answer.AnswerRepository;
import org.kosa.sbb.question.Question;
import org.kosa.sbb.question.QuestionRepository;
import org.kosa.sbb.question.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class SbbApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testExam() {
		int a = 10;
		int b = 20;
		int sum = add(a, b);
		assertEquals(30, sum);
	}

	private int add(int a, int b) {
		return a + b;
	}
	
	@Autowired
	QuestionRepository questionRepository; 

	@Autowired
	AnswerRepository answerRepository; 

	@Test
	void testJpa() {
		//sql -> insert into question (subject, content, create_date) values ('', '', now());
		//1. 객체 생성 -> Question entity
		//2. 생성된 객체를 디비에 추가(등록)을 한다 -> 저장소에 추가 -> QuestionRepository 
		
//		Question q1 = new Question();
//		q1.setSubject("sbb가 무엇인가요?");
//		q1.setContent("sbb에 대하여 알고 싶습니다.");
//		q1.setCreateDate(LocalDateTime.now());
//		
//		questionRepository.save(q1);
//		
//		Question q2 = new Question();
//		q2.setSubject("스프링 부트 모델 질문입니다");
//		q2.setContent("id는 자동으로 생성되나요?");
//		q2.setCreateDate(LocalDateTime.now());
//		
//		questionRepository.save(q2);
		
//		List<Question> all = questionRepository.findAll();
//		assertEquals(2, all.size());
//		
//		Question q = all.get(0);
//		assertEquals("sbb가 무엇인가요?", q.getSubject());
		
//		Optional<Question> oq = questionRepository.findById(32);
//		if (oq.isPresent()) {
//			Question q = oq.get();
//			assertEquals("sbb가 무엇인가요?", q.getSubject());
//		}
		
//		Question q = questionRepository.findBySubject("sbb가 무엇인가요?");
//		if (q != null) {
//			assertEquals(31, q.getId());
//			assertEquals("sbb가 무엇인가요?", q.getSubject());
//		}
		
//		Question q = questionRepository.findByContent("sbb에 대하여 알고 싶습니다.");
//		if (q != null) {
//			log.info(q.toString());
//			assertEquals(31, q.getId());
//			assertEquals("sbb가 무엇인가요?", q.getSubject());
//		}

//		List<Question> list = questionRepository.findBySubjectLike("%sb%");
//		if (list.size() > 0) {
//			Question q = list.get(0);
//			log.info(q.toString());
//			assertEquals(31, q.getId());
//			assertEquals("sbb가 무엇인가요?", q.getSubject());
//		}
		
//		Optional<Question> oq = questionRepository.findById(31);
//		if (oq.isPresent()) {
//			Question q = oq.get();
//			q.setSubject("수정된 제목");
//			questionRepository.save(q); //이 부분이 등록 또는 수정 구문을 실행하는 것임  
//		}

//		List<Answer> list = answerRepository.findByQuestionIdAndIdGreaterThan(32, 19);
//		for (var item : list) {
//			log.info("answer.id = {}", item.getId());
//		}
//		assertEquals(3, list.size());

	}
	
	@Autowired
	QuestionService questionService;
	
	@Test
	void 질문300건등록() {
		for (int i=1;i<=300;i++) {
			questionService.create(String.format("테스트 데이터 입니다:[%3d]", i), "내용 없음");
		}
	}
	
}
