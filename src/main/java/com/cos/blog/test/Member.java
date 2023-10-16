package com.cos.blog.test;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//전체 생성자 만들기
@AllArgsConstructor
//빈 생성자 만들기
@NoArgsConstructor
public class Member {
	
	//test sample model
	private int id;
	private String username;
	private String password;
	private String email;
	

}
