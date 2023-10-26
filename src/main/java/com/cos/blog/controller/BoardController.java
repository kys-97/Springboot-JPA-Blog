package com.cos.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {
	
	//url에 아무것도 적지 않거나, / 가 붙어있을 때 index(main) page로 이동한다
	@GetMapping({"","/"})
	public String index() {
		
		//WEB-INF/view/index.jsp
		return "index";
	}

}
