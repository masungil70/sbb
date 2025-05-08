package org.kosa.sbb.question;

import org.kosa.sbb.answer.AnswerForm;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
public class QuestionController {
	
	private final QuestionService questionService;
	
	@GetMapping("/list")
	public String list(Model model ,
			@RequestParam(value="page", defaultValue = "0") int page) {
		Page<Question> paging = questionService.getList(page);
		model.addAttribute("paging", paging);
		
		int startPage = ((page) / 10) * 10;
		int endPage = ((page) / 10) * 10 + 9;
		if (endPage > paging.getTotalPages()) endPage = paging.getTotalPages();
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		return "question_list";
	}
	
	@GetMapping("/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
		Question question = questionService.getQuestion(id);
		model.addAttribute("question", question);
		return "question_detail";
	}
	
	@GetMapping("/create")
	public String create(QuestionForm questionForm) {
		return "question_form";
	}
	
	@PostMapping("/create")
	public String create(@Valid QuestionForm questionForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "question_form";
		}
		questionService.create(questionForm.getSubject(), questionForm.getContent());
		return "redirect:/question/list";
	}
}
