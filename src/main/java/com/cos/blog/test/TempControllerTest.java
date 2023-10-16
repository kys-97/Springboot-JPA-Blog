package com.cos.blog.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//데이터가 아닌 파일을 리턴
@Controller 
public class TempControllerTest {
	
	//http://localhost:8000/blog/temp/home
	@GetMapping("/temp/home")
	public String tempHome() {
		System.out.println("tempHome()");
		//return "home.html"; -> 경로가 잘못 되었기 때문에 오류 발생
		return "/home.html";
		
		/*
		 * 동작은 하지만 JSP file [/WEB-INF/views/home.html.jsp] not found 이 오류 발생
		 *   mvc:
    view:
      prefix: /WEB-INF/views/
      suffix: .jsp
      yml 파일의 이 부분 때문.
		 * */
		
		/*
		 * 스프링부트는 jsp를 인식하지 못하기 때문에 pom.xml에 의존성을 주입해야 함
		 * */
	}
	
	@GetMapping("/temp/jsp")
	public String tempJsp() {
		
		//refix: /WEB-INF/views/
	    //suffix: .jsp
		//fullname: /WEB-INF/views/test.jsp
		return "test";
	}
	
}
