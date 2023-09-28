package com.cos.blog.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//스프링이 com.cos.blog 이하를 스캔해 모든 파일을 메모리에 new 하는 것이 아닌
//특정 어노테이션이 붙어있는 클래스 파일들을 new(IoC)해서 스프링 컨테이너에서 관리해줌
@RestController
public class BlogControllerTest {
	
	//실행 시 console창에 뜨는 메시지
	//2023-08-06 17:19:28.305  INFO 11032 --- [  restartedMain] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
	//따라서 http://localhost:8080/test/hello 요청 시 문자가 브라우저에 찍힘
	@GetMapping("/test/hello")
	public String hello() {
		return "<h1>hello spring boot</h1>";
	}
	

}
