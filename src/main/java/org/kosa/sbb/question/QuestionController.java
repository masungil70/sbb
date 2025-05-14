package org.kosa.sbb.question;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.kosa.sbb.answer.AnswerForm;
import org.kosa.sbb.user.SiteUser;
import org.kosa.sbb.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
 * 80% spring boot 제공하는 클래스 
 * 20% jdk api 사용(util, collection, format)  
 * 경우에 따라서 제3자가 제공하는 클래스 (라이브러리) -> 직접 사용하는 것 
 *      -> wrapper class로 작성하는 방법  
 *      
 * 
 * spring boot 에서 제공하는 Paging 관한 클래스 -> nav Paging 부분
 *    
 * 
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("/question")
@Slf4j
public class QuestionController {
	
	private final QuestionService questionService;
	private final UserService userService;

	@GetMapping("/list")
	public String list(Model model 
			, @RequestParam(value="page", defaultValue = "0") int page
			, @RequestParam(value="kw", defaultValue = "") String kw
			) {
//		Page<Question> paging = questionService.getList(page);
//		model.addAttribute("paging", paging);
//		
//		int startPage = ((page) / 10) * 10;
//		int endPage = ((page) / 10) * 10 + 9;
//		if (endPage > paging.getTotalPages()) endPage = paging.getTotalPages();
//		model.addAttribute("startPage", startPage);
//		model.addAttribute("endPage", endPage);
		
		model.addAttribute("paging", questionService.getList(page, kw));
		model.addAttribute("kw", kw);
		
		return "question_list";
	}
	
	@PreAuthorize("isAuthenticated")
	@GetMapping("/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
		Question question = questionService.getQuestion(id);
		model.addAttribute("question", question);
		return "question_detail";
	}
	
	@PreAuthorize("isAuthenticated")
	@GetMapping("/create")
	public String create(QuestionForm questionForm) {
		return "question_form";
	}

	
		
	@PreAuthorize("isAuthenticated")
	@GetMapping("/modify/{id}")
	public String modify(QuestionForm questionForm
			, @PathVariable("id") Integer id
			, Principal principal /*로그인한 사용자 정보 객체*/) {
		//1. 질문 아이디로 질문 객체를 얻는다
		Question question = questionService.getQuestion(id);
		
		//2. 질문작성자와 로그인한 사용자가 같지 않으면 권한 예외 발생하고 끝
		if (!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		//3. 디비에서 얻는 질문의 상세 정보를 화면 출력 할 수 있게 설정한다
		questionForm.setContent(question.getContent());
		questionForm.setSubject(question.getSubject());
		
		return "question_form";
	}
	
	@PreAuthorize("isAuthenticated")
	@GetMapping("/delete/{id}")
//	@ResponseBody
//	public Map<String, Object> delete(Principal principal /*로그인한 사용자 정보 객체*/
	public String delete(Principal principal /*로그인한 사용자 정보 객체*/
			, @PathVariable("id") Integer id /*경로를 사용하여 전달된 질문 아이디*/) {
		Map<String, Object> result = new HashMap<String, Object>();
		//1. 질문 아이디를 이용하여 상세 정보를 얻는다
		Question question = questionService.getQuestion(id);
		//2. 질문작성자와 로그인한 사용자가 같지 않으면 권한 예외 발생하고 끝
		if (!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		//3. 질문 정보를 수정한다 
		questionService.delete(question);

//		result.put("status", "ok");
//		return result;
		return "redirect:/question/list";
	}
	
	@PreAuthorize("isAuthenticated")
	@GetMapping("/vote/{id}")
	public String questionVote(Model model
			, @PathVariable("id") Integer id /*질문 아이디*/
			, Principal principal /*로그인한 사용자 정보 객체*/ ) {
		Question question = questionService.getQuestion(id); /* 질문 아이디를 이용하여 질문 객체 얻기 */
		//로그인 사용한 정보 얻기 
		SiteUser siteUser = userService.getUser(principal.getName());
		//추천인 추가 
		questionService.vote(question, siteUser);
		
		return "redirect:/question/detail/" + id; //상세보기로 이동한다 
	}
	

}
