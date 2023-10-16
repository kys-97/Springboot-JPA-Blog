package com.cos.blog.test;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//사용자가 요청 -> HTML로 응답 -> CONTROLLER

//사용자가 요청 -> DATA로 응답 -> REST CONTROLLER
@RestController
public class HttpControllerTest {
	
	private static final String TAG = "HttpControllerTest:";
	
	//localhost:8000/blog/http/lombok
	@GetMapping("/http/lombok")
	public String lombokTest() {
		Member m = new Member(1, "kim", "1234", "email");
		System.out.println(TAG+"getter: "+m.getId());
		m.setId(1234);
		System.out.println(TAG+"setter: "+m.getId());
		return "lombok test fin";
	}

	//http://localhost:8080/http/get -> select
	@GetMapping("/http/get")
	public String getTest(Member m) {
		
		return "Get 요청: "+m.getId()+","+m.getUsername()+","+m.getPassword()+","+m.getEmail();
		
		//http://localhost:8080/http/get?id=1 -> ?id=1 < 쿼리스트링
		//파라미터로 주어야 함
	}
	
	//http://localhost:8080/http/post -> insert -> 데이터 받아야 함
	@PostMapping("/http/post")
	public String postTest(@RequestBody Member m) {
		return "post 요청: "+m.getId()+","+m.getUsername()+","+m.getPassword()+","+m.getEmail();
		
		//get과 달리 body에 붙여서 보냄
		//두 가지 방법이 있음. 
		//1. x-www-form-urlencoded
		//row
	}
	
	//http://localhost:8080/http/put -> update
	@PutMapping("/http/put")
	public String putTest(@RequestBody Member m) {
		return "Put 요청"+m.getId()+","+m.getUsername()+","+m.getPassword()+","+m.getEmail();
	}
	
	//http://localhost:8080/http/delete -> delete
	@DeleteMapping("/http/delete")
	public String deleteTest() {
		return "Delete 요청";
	}
	

}
