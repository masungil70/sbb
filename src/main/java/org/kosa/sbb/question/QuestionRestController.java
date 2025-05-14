package org.kosa.sbb.question;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.kosa.sbb.answer.AnswerForm;
import org.kosa.sbb.user.SiteUser;
import org.kosa.sbb.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question")
@Slf4j
public class QuestionRestController {
	
	private final QuestionService questionService;
	private final UserService userService;

	@PreAuthorize("isAuthenticated")
	@PostMapping("/create")
	public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody QuestionForm questionForm
			, BindingResult bindingResult
			, Principal principal /*로그인한 사용자 정보 객체*/) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (bindingResult.hasErrors()) {
			result.put("status", "error");
			result.put("errorMessage", "내용은 필수항목입니다.");
			return  ResponseEntity.ok().body(result);
		}
		//로그인 한 사용자 정보를 얻는다 
		SiteUser siteUser = userService.getUser(principal.getName());
		questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);

		result.put("status", "ok");
		return ResponseEntity.ok().body(result); 
	}

	@PreAuthorize("isAuthenticated")
	@PostMapping("/modify/{id}")
	public ResponseEntity<Map<String, Object>> modify(@Valid @RequestBody QuestionForm questionForm
			, BindingResult bindingResult
			, Principal principal /*로그인한 사용자 정보 객체*/
			, @PathVariable("id") Integer id /*경로를 사용하여 전달된 질문 아이디*/) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (bindingResult.hasErrors()) {
			result.put("status", "error");
			result.put("errorMessage", "내용은 필수항목입니다.");
			return ResponseEntity.ok().body(result);
		}
		//1. 질문 아이디를 이용하여 상세 정보를 얻는다
		Question question = questionService.getQuestion(id);
		//2. 질문작성자와 로그인한 사용자가 같지 않으면 권한 예외 발생하고 끝
		if (!question.getAuthor().getUsername().equals(principal.getName())) {
			//throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("status", "error", "errorMessage", "수정 권한이 없습니다."));
		}
		//3. 질문 정보를 수정한다 
		questionService.modify(question, questionForm.getSubject(), questionForm.getContent());

		result.put("status", "ok");
		return ResponseEntity.ok().body(result); 
	}
	
}
