package org.kosa.sbb.answer;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kosa.sbb.question.Question;
import org.kosa.sbb.question.QuestionService;
import org.kosa.sbb.user.SiteUser;
import org.kosa.sbb.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
 
/* MVC model 
 * Controller -> Service -> DAO(Repository) 
 *   <- View (model에서 추가)
 *   
 * client (fetch, 비동기 함수) -> rest api (data(json) 수신 처리)     
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/answer")
@Slf4j
public class AnswerController {
	
	private final QuestionService questionService;
	private final AnswerService answerService;
	private final UserService userService;
	
	@PreAuthorize("isAuthenticated")
	@PostMapping("/create/{id}")
	@ResponseBody
	public Map<String, Object> create(@PathVariable("id") Integer id
			, @Valid @RequestBody AnswerForm answerForm
			, HttpServletRequest request
			, BindingResult bindingResult
			, Principal principal /*로그인한 사용자 정보 객체*/) {
		Map<String, Object> result = new HashMap<String, Object>();
		//principal.getName(); //로그인한 사용자이름(아이디)
		//로그인 한 사용자 정보를 얻는다 
		SiteUser siteUser = userService.getUser(principal.getName());
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
		answerService.create(question, answerForm.getContent(), ip, siteUser);
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
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id, Principal principal) {
		Answer answer = this.answerService.getAnswer(id);
		if (!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		answerForm.setContent(answer.getContent());
		return "answer_form";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult,
			@PathVariable("id") Integer id, Principal principal) {
		if (bindingResult.hasErrors()) {
			return "answer_form";
		}
		Answer answer = this.answerService.getAnswer(id);
		if (!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		this.answerService.modify(answer, answerForm.getContent());
		return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String answerDelete(Principal principal, @PathVariable("id") Integer id) {
		Answer answer = answerService.getAnswer(id);
		if (!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
		}
		this.answerService.delete(answer);
		return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
	}	

	@PreAuthorize("isAuthenticated")
	@GetMapping("/vote/{id}")
	public String answerVote(Model model
			, @PathVariable("id") Integer id /*답변 아이디*/
			, Principal principal /*로그인한 사용자 정보 객체*/ ) {
		Answer answer = answerService.getAnswer(id); /* 답변 아이디를 이용하여 답변객체 얻기 */
		//로그인 사용한 정보 얻기 
		SiteUser siteUser = userService.getUser(principal.getName());
		//추천인 추가 
		answerService.vote(answer, siteUser);
		
		return "redirect:/question/detail/" + answer.getQuestion().getId() + "#answer_" + answer.getId(); //상세보기로 이동한다 
	}

}
