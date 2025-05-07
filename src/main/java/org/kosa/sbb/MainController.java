package org.kosa.sbb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

/* MVC
 * 
 * M : model (B/L) 
 * V : view  (jsp, html:타임리프, spring boot에서 view의 표준으로 html : 타임리프 사용 추천 )
 * C : controller 
 */
@Controller
@Slf4j
public class MainController {

	@GetMapping("/")
	public String root() {
		return "redirect:/question/list"; 
	}

//	/detail?id=1
//	/detail/1
//	@GetMapping("/detail/{id}")
//	public String func(String id) {
//		return "redirect:/question/list"; 
//	}

		
	//view을 사용하지 않고 응답 하는 방법 
	@GetMapping("/sbb")
	@ResponseBody
	public String index() {
		System.out.println("index....");

		log.trace("trace 로그 ...");
		log.debug("debug 로그 ...");
		log.info("info 로그...");
		log.warn("warn 로그 ...");
		log.error("error 로그...");
		
		//@ResponseBody 이 없을 때는 static/templates/index.html 문서를 의미한다
		//@ResponseBody 있으면 순수한 값이 브라우저로 리턴된다 
		return "안녕하세요 sbb에 오신것을 환영합니다. ~~~~"; 

	}
}
