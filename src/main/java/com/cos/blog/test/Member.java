package com.cos.blog.test;

public class Member {
	
	//test sample model
	private int id;
	private String username;
	private String password;
	private String email;
	
	//생성자
	public Member(int id, String username, String password, String email) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	/*
	 * 자바에서 변수를 다 private로 만드는 이유
	 * 예) people 이라는  model에 배고픔(hungryState) 가 있다면?
	 * 배고픔을 해결하고 싶다 -> people 객체를 만들어 배고픔을 100으로 늘리면 됨 -> 변수에 바로 접근해 변수의 값을 바꿈. -> 객체지향과 맞지 않음
	 * 따라서 무언가를 먹는다는 함수 (eat) 함수를 실행하면서 배고픔의 값을 바꿔야 됨. 변수에 바로 접근해서는 안되기 때문. 따라서 변수가 아니라 메소드를 public으로 만들어 메소드를 통해 값을 변화시켜야 함
	 * */
	
	//private 변수의 상태값을 변경할 수 있는 getter / setter
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	

	
	

}
