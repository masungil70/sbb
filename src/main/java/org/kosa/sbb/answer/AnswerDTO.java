package org.kosa.sbb.answer;

import java.time.LocalDateTime;

import org.kosa.sbb.CommonUtil;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class AnswerDTO {
	private Integer id;
	private String content;
	private LocalDateTime createDate;
	private String author;
	private int voterSize;
	
	static CommonUtil commonUtil = new CommonUtil(); 
	
	public static AnswerDTO of(Answer answer) {
		
		return new AnswerDTO(answer.getId(), commonUtil.markdown(answer.getContent()), answer.getCreateDate(), answer.getAuthor().getUsername(), answer.getVoter().size());
	}
}
