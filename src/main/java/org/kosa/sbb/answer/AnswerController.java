package org.kosa.sbb.answer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kosa.sbb.question.Question;
import org.kosa.sbb.question.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/answer")
@Slf4j
public class AnswerController {
	
	private final QuestionService questionService;
	private final AnswerService answerService;
	
	@PostMapping("/create/{id}")
	@ResponseBody
	public Map<String, Object> create(@PathVariable("id") Integer id
			, @Valid @RequestBody AnswerForm answerForm
			, HttpServletRequest request
			, BindingResult bindingResult) {
		Map<String, Object> result = new HashMap<String, Object>();
		Question question = questionService.getQuestion(id);

		//스프링부트에서 어떤 위치에서든 HttpServletRequest를 얻고자 할 때 아래 코드를 사용할 수 있음 
		//아래 코드는 컨트롤 메소드에서 인자로 HttpServletRequest request을 받지 않고 직접 어떤 위치든 사용할 수 있는 방법임 
		//HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		
		log.info("answerForm.content = [" + answerForm.getContent() + "]");
		if (bindingResult.hasErrors()) {
			result.put("status", "error");
			result.put("errorMessage", "내용은 필수항목입니다.");
			return result; 
		}
		//ip을 설정한다
		String ip = request.getRemoteAddr();
		
		//디비에 저장한다 
		answerService.create(question, answerForm.getContent(), ip);
		result.put("status", "ok");
		return result; 
	}
	
	@GetMapping("/list/{questionId}")
	@ResponseBody
	public Map<String, Object> list(
				@PathVariable("questionId") Integer questionId, 
				@RequestParam(name = "id", defaultValue = "0") Integer id) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", "ok");
		try {
			result.put("answerList", answerService.findByQuestionIdAndIdGreaterThan(questionId, id));
		} catch(Exception e) {
			result.put("answerList", List.of());
			result.put("status", "error");
		}
		return result;
	}
	
}
