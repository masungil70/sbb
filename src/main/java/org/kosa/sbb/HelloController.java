package org.kosa.sbb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
	@GetMapping("/hello")
	@ResponseBody
	public String hello() {
		return "Hello world ~~~ ^.^ @@@";
	}

	@GetMapping("/test") 
	public String test() {
		return "test";
	}
}
