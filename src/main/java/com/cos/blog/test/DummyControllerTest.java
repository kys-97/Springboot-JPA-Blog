package com.cos.blog.test;

import java.util.List;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.UserInfo;
import com.cos.blog.repository.UserInfoRepository;

@RestController // 응답만 (HTML이 아닌 DATA를 리턴해주는 컨트롤러)
public class DummyControllerTest {

	// UserInfoRepository import
	// userRepository: 스프링이 restController를 읽어서 더미컨트롤러를 띄워줄 때 이 값은 null 따라서
	// autowired라는 어노테이션 필요
	@Autowired // 의존성 주입(Dependency Injection)
	private UserInfoRepository userRepository;

	// select test
	// 전체를 선택할 것이기 때문에 파라미터를 받을 필요 없이 리턴만 하면 됨
	@GetMapping("/dummy/user")
	public List<UserInfo> list() {
		// findAll() 전체 리턴
		return userRepository.findAll();

		// JSON VIEW 확장프로그램을 설치해 주소창에 링크 입력 후 JSON 확인
		// 배열로 들어가 있는 것을 확인할 수 있음
	}

	@GetMapping("/dummy/user/page")
	// pageable: data.domain
	// Page로 메소드 결과 받기
	// 주소 뒤에 파라미터로 페이지를 받아 페이지를 넘길 수 있음. page?page={id}
	// 2개씩 / id기준으로 정렬
	public List<UserInfo> pageList(
			@PageableDefault(size = 2, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<UserInfo> pagingUser = userRepository.findAll(pageable);
		// 조건문 사용해 분기처리 가능
		List<UserInfo> users = pagingUser.getContent();
		return users;
	}

	// 상세보기
	// 주소로 id 파라미터 전달 받을 수 있음

	@GetMapping("/dummy/user/{id}")
	public UserInfo detail(@PathVariable int id) {
		// user 4를 찾았을 때, 내가 데이터베이스에서 못 찾아오게 되면 user가 null?
		// 그럼 return 시 null이 리턴이 되는데 프로그램에 문제가 됨
		// Optional로 너의 UserInfo 객체를 감싸서 가져올테니 null인지 아닌지 판단해 return

		// 잘못된 인수(데이터베이스에 없는 아이디)가 들어올 시 throw로 날려달라
		UserInfo user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
			@Override
			public IllegalArgumentException get() {
				//해당 유저는 없습니다. id: 4
				return new IllegalArgumentException("해당 유저는 없습니다. id: " + id);
			}
		});
		
		//IllegalArgumentException가 발생하면 스프링에서 AOP를 통해 에러페이지를 보여주게 됨
		
		//웹브라우저가 요청한 USER 객체(자바 오브젝트)를 응답
		//자바가 들고있는 오브젝트를 웹브라우저는 이해를 못함
		//따라서 USER객체를 웹브라우저가 이해할 수 있는 데이터로 변환을 해야 하는데, 가장 좋은 방법이 JSON을 이용하는 것
		//스프링부트 = MessageConverter가 응답 시 자동으로 작동
		//if 자바 오브젝트를 리턴하면 MessageConverter가 Jackson 라이브러리를 호출해 user 오브젝트를 json으로 변환해 브라우저에 던져줌
		return user;
	}

	// insert -> postMapping
	@PostMapping("/dummy/join")
	// http://localhost:8000/blog/dummy/join(요청)
	// http의 body에 username password email를 요청하면 굳이 RequestParam을 사용하지 않아도 값이 제대로
	// 들어옴
	public String join(UserInfo userinfo) {
		// 매개변수의 변수를 적기만 하면 값을 집어넣어 준다는 것
		// key = value(약속된 규칙) -> 함수의 파라미터로 파싱해서 집어넣어줌 String username, String password,
		// String email
		// object로 받을 수 있음
		System.out.println("username: " + userinfo.getUsername());
		System.out.println("password: " + userinfo.getPassword());
		System.out.println("email: " + userinfo.getEmail());
		System.out.println("role: " + userinfo.getRole()); // default value = userInfo임. 쿼리 상 insert할 때 null값으로 들어가게 됨.
		// 그렇다면 null값이 아닌 다른 값으로 들어가게 하는 방법은? DynamicInsert 어노테이션을 UserInfoRepository에
		// 붙임
		System.out.println("createDate: " + userinfo.getCreateDate()); // 자바에서 만들어서 insert

		// 다른 정보는 다 들어갔지만 role 데이터는 들어가지 않음. -> default 값 제거 후 setRole을 통해 user
		userinfo.setRole(RoleType.USER);
		userRepository.save(userinfo);
		return "회원가입이 완료되었습니다.";
	}

//	// update: email, password
//	@PutMapping("/dummy/user/{id}")
//	// password, email -> form tag 안의 정보를 insert로 받아올 수도 있지만, json으로 받아올 땐
//	// requestbody
//	public UserInfo updateUser(@PathVariable int id, @RequestBody UserInfo requestUser) {
//		System.out.println("id: " + id);
//		System.out.println("password: " + requestUser.getPassword());
//		System.out.println("email: " + requestUser.getEmail());
//		/*
//		 * - request JSON data
//		 * { "password":"5678", "email": "ssar.gmail.com" }
//		 * 
//		 * 
//		 * - console
//		 * id: 1 password: 5678 email: ssar.gmail.com
//		 * 
//		 * JSON 데이터를 요청하면 Message converter의 Jackson lib가 Java Object로 변환해서 받아줌 -> 이때 필요한 어노테이션: @RequestBody
//		 */
//		
//		
//		/*
//		 * 아래의 경우 오류 발생(DataIntegrityViolationException)
//		 * 500 오류
//		 * 데이터가 위배됨
//		 * save 시 username이 null이 되기 때문. password, email 만 수정하고 싶은데 UserInfo 객체를 인스턴스화 시킨 requestUser로 파라미터를 받게 될 경우 
//		 * UserInfo 안의 값들 중 password, email의 값을 제외하고 모두 null값으로 들어오게 됨.
//		 * */
//		//save method?
//		//ID SET
////		requestUser.setId(id);
////		//강제로 userName set
////		requestUser.setUsername("Hello");
////		/*
////		 * 강제로 userName을 입력한 후 update 실행
////		 * 이후 데이터베이스 확인하면 1		ssar.gmail.com	5678		Hello 이렇게 null값이 컬럼으로 들어가게 됨
////		 * 기존의 1번 아이디를 찾아 입력한 이메일, 패스워드를 수정함. 그러나 입력받지 않은 값은 모두 null로 들어가는 문제가 발생함
////		 * 따라서 update를 할 때는 save를 통해 하지 않음
////		 * 
////		 * */
////		userRepository.save(requestUser);
//		
//		//save를 통해 update를 하는 방법
//		//1. userRepository를 통해 id를 찾음
//		//2. 오류가 발생할 수 있으니 orElseThrow를 이용
//		UserInfo user = userRepository.findById(id).orElseThrow(()->{
//			return new IllegalArgumentException("수정에 실패하였습니다.");
//		});
//		//userInfo 인스턴스에 set 하면 정상적으로 update 가능 -> null이 없기 때문
//		user.setPassword(requestUser.getPassword());
//		user.setEmail(requestUser.getEmail());
//		userRepository.save(user);
//		
//		//수정 성공(21번)
////		{
////		    "password":"9898",
////		    "email": "love.gmail.com"
////		}
////		
//		return null;
///*
// * save 함수는 id를 전달하지 않으면 insert
// * id를 전달했을 때 해당 id에 대한 데이터가 있으면 update
// * id를 전달했을 때 해당 id에 대한 데이터가 없으면 insert
// * */
//	}

	// 위의 방법은 db에서 userInfo 객체의 데이터만 바꾼 것
	// transactional annotation -> save 메소드 사용 없이도 업데이트 가능
	// update: email, password
	@Transactional
	@PutMapping("/dummy/user/{id}")
	// password, email -> form tag 안의 정보를 insert로 받아올 수도 있지만, json으로 받아올 땐
	// requestbody
	public UserInfo updateUser(@PathVariable int id, @RequestBody UserInfo requestUser) {
		System.out.println("id: " + id);
		System.out.println("password: " + requestUser.getPassword());
		System.out.println("email: " + requestUser.getEmail());

		UserInfo user = userRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("수정에 실패하였습니다.");
		});

		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		// userRepository.save(user);
		// 22번 user 수정
		// 기존 정보: Bye.example.com / 1234
		// 수정 성공: ByeBye.gmail.com 5848

		// 더티체킹
		return user;
		// user를 리턴해 update에 대한 결과를 볼 수 있도록(21번 데이터 수정)
//		{
//		    "id": 21,
//		    "username": "Hello",
//		    "password": "5555",
//		    "email": "GoodBye.gmail.com",
//		    "role": "USER",
//		    "createDate": "2023-10-19T12:50:58.737+00:00"
//		}

	}

	// delete
	// 1번 delete
	@DeleteMapping("/dummy/user/{id}")
	public String delete(@PathVariable int id) {
		try {
			userRepository.deleteById(id);

		} catch (EmptyResultDataAccessException e) {
			// 5번 삭제: 삭제에 실패하였습니다. 해당 id는 데이터베이스에 없습니다.
			return "삭제에 실패하였습니다. 해당 id는 데이터베이스에 없습니다.";
		}

		return "삭제 되었습니다. id: " + id;
	}
	
	/*
	 * 1. get 요청 (select 할 때 사용)
	 * http://localhost:8000/blog/user?username=ssar
	 * 특징: body로 데이터를 담아 보내지 않음.
	 * 
	 * 2. post, put, delete 요청 (데이터를 변경)
	 * 특징
	 * - 전송해야 할 데이터가 많음
	 * - post: form 태그에 method='post' 로 넣어 요청할 수 있음
	 * form tag: get, post 요청만 가능 (key = value) 형태로 전송됨
	 * form 태그의 한계 때문에 자바스크립트로 요청하는 것이 나음
	 * 
	 * 통일: 자바스크립트로 ajax 요청 + 데이터는 json으로 통일
	 * 
	 * 스프링에 form:form 태그 -> post, put, delete, get 요청이 가능한 태그
	 * 
	 * 3. 오브젝트로 데이터 받기
	 * post 방식의 key = value (x-www-form-urlencoded)
	 * 
	 * 
	 * 
	 * */

}
