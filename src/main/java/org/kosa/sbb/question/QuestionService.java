package org.kosa.sbb.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.kosa.sbb.DataNotFountException;
import org.kosa.sbb.answer.Answer;
import org.kosa.sbb.page.PageResponseVO;
import org.kosa.sbb.user.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
	private final QuestionRepository questionRepository;

//	public Question getQuestion(Integer id) {
//		Optional<Question> oq = questionRepository.findById(id);
//		if (oq.isPresent()) {
//			return oq.get();
//		}
//		else {
//			throw new DataNotFountException("question not found");
//		}
//	}
	
	private Specification<Question> search(String kw) {
		return new Specification<Question>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {

				query.distinct(true);
				Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT); 
				Join<Question, Answer> a = q.join("answerList", JoinType.LEFT); 
				Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
				
				return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), 
						cb.like(q.get("content"), "%" + kw + "%"),
						cb.like(u1.get("username"), "%" + kw + "%"),
						cb.like(a.get("content"), "%" + kw + "%"),
						cb.like(u2.get("username"), "%" + kw + "%"));
			}
		};
	}
	
	public Question getQuestion(Integer id) {
		return questionRepository.findById(id)
				.orElseThrow(() -> new DataNotFountException("question not found"));
//		
//		if (oq.isPresent()) {
//			return oq.get();
//		}
//		else {
//			throw new DataNotFountException("question not found");
//		}
	}

	public PageResponseVO<Question> getList(int page, String kw) {
		//정렬 객체 생성 
		List<Sort.Order> sorts = new ArrayList<Sort.Order>();
		
		//정려 필드와 방법을 설정함 
		sorts.add(Sort.Order.desc("createDate"));

		//페이지 요청 객체에 정렬 객체 추가함 
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		Specification<Question> spec = search(kw);
		return new PageResponseVO<Question>(questionRepository.findAll(spec, pageable));
	}
	

	public List<Question> findAll() {
		return questionRepository.findAll();
	}
	
	public void create(String subject, String content, SiteUser siteUser) {
		Question question = new Question();
		question.setSubject(subject);
		question.setContent(content);
		question.setCreateDate(LocalDateTime.now());
		question.setAuthor(siteUser);
		
		questionRepository.save(question);
	}

	public void modify(Question question, String subject, String content) {
		question.setSubject(subject);
		question.setContent(content);
		question.setModifyDate(LocalDateTime.now());
		
		questionRepository.save(question);
		
	}

	public void delete(Question question) {
		questionRepository.delete(question);
	}

	public void vote(Question question, SiteUser siteUser) {
		if (question.getVoter().contains(siteUser)) {
			//추천인 삭제 
			question.getVoter().remove(siteUser);
		} else {
			//추천인 추가  
			question.getVoter().add(siteUser);
		}
		questionRepository.save(question);
	}	
}
