package org.kosa.sbb.question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.kosa.sbb.answer.Answer;
import org.kosa.sbb.user.SiteUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 200)
	private String subject;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private LocalDateTime createDate;
	
	@OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
	private List<Answer> answerList;
	
	public int getMaxAnswerId() {
		return answerList.isEmpty() ? 0 : answerList.get(answerList.size()-1).getId();
	}
	
	@ManyToOne
	private SiteUser author;
	
	private LocalDateTime modifyDate; //수정일시
	
	
	@ManyToMany
	Set<SiteUser> voter;
	
}	
