package org.kosa.sbb.answer;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class AnswerDTO {
	private Integer id;
	private String content;
	private LocalDateTime createDate;
	
	public static AnswerDTO of(Answer answer) {
		return new AnswerDTO(answer.getId(), answer.getContent(), answer.getCreateDate());
	}
}
