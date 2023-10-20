package com.cos.blog.test;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	//select test
	//전체를 선택할 것이기 때문에 파라미터를 받을 필요 없이 리턴만 하면 됨
	@GetMapping("/dummy/user")
	public List<UserInfo> list(){
		//findAll() 전체 리턴
		return userRepository.findAll();
		
		//JSON VIEW 확장프로그램을 설치해 주소창에 링크 입력 후 JSON 확인
		//배열로 들어가 있는 것을 확인할 수 있음
	}
	
	@GetMapping("/dummy/user/page")
	//pageable: data.domain
	//Page로 메소드 결과 받기
	// 주소 뒤에 파라미터로 페이지를 받아 페이지를 넘길 수 있음. page?page={id}
	//2개씩 / id기준으로 정렬
	public List<UserInfo> pageList(@PageableDefault(size = 2, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<UserInfo> pagingUser = userRepository.findAll(pageable);
		//조건문 사용해 분기처리 가능
		List<UserInfo> users = pagingUser.getContent();
		return users;
	}
	
	
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
	
	//update test
	/*
	 * Transactional
	 * 영속성 컨텍스트
	 * controller에서 insert, update, delete, select 등의 동작을 요청에 따라 수행
	 *ex) JPA 영속성 컨텍스트 > user controller - SAVE 동작 수행
	 *- 영속성 컨텍스트 안의 1차 캐시에 SAVE 동작 수행 시 저장한 USER 객체가 쌓임
	 *- 만들어진 USER 객체를 DATABASE에 새로운 컬럼으로 저장
	 *-> 1차 캐시에 USER 가 생성된 것을 "영속화"되었다고 표현함
	 *"영속화"된 USER 객체를 DB에 밀어넣는 과정을 "FLUSH"라고 함
	 *FLUSH? -> 쌓여있는 데이터를 더 큰 창고(데이터베이스)로 옮겨두고 JPA 안의 캐시를 (=버퍼를) 비우는 동작
	 *특징: 1차 캐시가 날아가진 않고 남아있음. 영속화 된 객체의 1차 캐시를 남겨둔 채로 컨트롤러에서 CRUD 동작을 수행하는 식으로 프로그램의 부하를 줄임
	 *
	 *UPDATE를 하고 싶다면?
	 *UPDATE를 하고 싶은 데이터: 23번 데이터의 PASSWORD
	 *23번 데이터를 데이터베이스에서 들고와 JPA 영속성 컨텍스트에 캐시화 시킨 후 컨트롤러에서 해당 오브젝트의 값을 UPDATE 메소드를 활용해 변경
	 *- USER 오브젝트의 값을 변경한 동작을 수행한 후 SAVE를 호출할 시 (비밀번호와 이메일 변경) 영속화 된 오브젝트와 DB에 있는 오브젝트를 비교해 보았을 때 가지고 있는 PK값이 같음
	 *그 경우 영속화된 USER객체에서 수정한 값을 UPDATE 시킴. 그 후 변경된 USER객체의 값을 FLUSH를 통해 밀어넣음. 이 과정이 SAVE를 수행할 때 일어남
	 *
	 *Transactional 어노테이션을 붙이면 컨트롤러의 UPDATE 메소드가 호출 - 리턴될 때 트랜잭션이 실행 - 종료됨. 그리고 이는 함수 종료 시에 자동으로 COMMIT이 되면서 UPDATE 기능 수행함
	 *UPDATE METHOD에 @TRANSACTIONAL 어노테이션을 걸면 종료 시 커밋이 됨
	 *-> 영속성 컨텍스트에 23번 데이터를 데이터베이스에서 가지고 와 1차 캐시에 넣어둠
	 *-> SET을 통해 PW, EMAIL 변경 후 컨트롤러 종료. 이후 영속화 된 USER 오브젝트가 트랜잭션 어노테이션에 의해 변경이 인식됨. 이후 컨트롤러 종료 시 데이터베이스에 업데이트문 수행 (USER 오브젝트의 값이 변경되었기 때문)
	 *
	 *만약 영속화만 시킨다면?
	 *SET을 통해 변경된 것을 알려주지 않는다면 영속화 된 객체의 수정 사항이 확인되지 않아 업데이트가 수행되지 않음
	 *
	 *이러한 방식으로 업데이트 기능을 수행하는 것을 "더티체킹" 이라고 함
	 * */
	@Transactional //save를 호출하지 않았는데 변경이 됨?
	//더티체킹? save를 하지 않아도 update가 가능 -> 앞으로 update 시 save 사용하지 않고 transactional 사용할 것
	@PutMapping("/dummy/user/{id}")
	//url이 같더라도 매핑된 요청이 get, put으로 다르기 때문에 관계 없음
	//email, password
	//json data test
	public UserInfo updateUser (@PathVariable int id, @RequestBody UserInfo requestUser) {
		System.out.println("id: "+id);
		System.out.println("password: "+requestUser.getPassword());
		System.out.println("email: "+requestUser.getEmail());
		
		//insert 시 쓰는 method 사용 -> 여기에 id를 set해서 사용 -> 오류 발생
		//save 할 때 필요한 정보들이 들어가는 것이 아니기 때문 -> 다른 값이 null로 들어가게 됨
		//requestUser.setId(id);
		//userRepository.save(requestUser);
		//따라서 update method 사용
		
		UserInfo user = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("fail update");
		});
		requestUser.setId(id);
		requestUser.setPassword(requestUser.getPassword());
		requestUser.setEmail(requestUser.getEmail());
		//save 함수는 id를 전달하면 해당 id에 대한 데이터가 있으면 update
		//해당 id에 대한 데이터가 없으면 insert
		//userRepository.save(requestUser);
		
		
		return null;
		
		/*
		 * 요청한 json data: 24
		 *       "id": 24,
      "username": "Who",
      "password": "1234",
      "email": "Who@example.com",
      "role": "USER",
      "createDate": "2023-10-19T12:51:25.046+00:00"
      
      수정된 데이터
      id: 24
password: 5678
email: Hi@example.com

json 데이터를 요청하면 그것을 Java Object로 변환해서 받아줌
메세지 컨버터의 잭슨 라이브러리가 변환해서 받아줌 -> 이때 필요한 어노테이션: 리퀘스트 바디
		 * */
	}

}
