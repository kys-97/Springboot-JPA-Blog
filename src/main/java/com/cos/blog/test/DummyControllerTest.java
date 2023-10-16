package com.cos.blog.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.UserInfo;
import com.cos.blog.repository.UserInfoRepository;

@RestController //응답만
public class DummyControllerTest {
	
	//UserInfoRepository import
	//userRepository: 스프링이 restController를 읽어서 더미컨트롤러를 띄워줄 때 이 값은 null 따라서 autowired라는 어노테이션 필요
	@Autowired //의존성 주입
	private UserInfoRepository userRepository;
	
	//insert -> postMapping
	@PostMapping("/dummy/join")
	//http://localhost:8000/blog/dummy/join(요청)
	//http의 body에 username password email를 요청하면 굳이 RequestParam을 사용하지 않아도 값이 제대로 들어옴
	public String join(UserInfo userinfo) {
		//매개변수의 변수를 적기만 하면 값을 집어넣어 준다는 것
		//key = value(약속된 규칙) -> 함수의 파라미터로 파싱해서 집어넣어줌 String username, String password, String email
		//object로 받을 수 있음
		System.out.println("username: "+userinfo.getUsername());
		System.out.println("password: "+userinfo.getPassword());
		System.out.println("email: "+userinfo.getEmail());
		System.out.println("role: "+userinfo.getRole()); //default value = userInfo임. 쿼리 상 insert할 때 null값으로 들어가게 됨.
		//그렇다면 null값이 아닌 다른 값으로 들어가게 하는 방법은? DynamicInsert 어노테이션을 UserInfoRepository에 붙임
		System.out.println("createDate: "+userinfo.getCreateDate()); //자바에서 만들어서 insert
		
		//다른 정보는 다 들어갔지만 role 데이터는 들어가지 않음.  -> default 값 제거 후 setRole을 통해 user
		userinfo.setRole(RoleType.USER);
		userRepository.save(userinfo);
		return "회원가입이 완료되었습니다.";
	}

}
