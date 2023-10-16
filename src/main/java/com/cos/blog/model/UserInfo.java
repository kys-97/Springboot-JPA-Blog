package com.cos.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//데이터를 테이블화 시키기 위해 Entity 어노테이션
@Entity //User class가 자동으로 오라클에 테이블로 생성이 됨
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder //빌더패턴
public class UserInfo {
	
	//회원가입 시 insert가 됨

	@Id //primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) //프로젝트에서 연결된 db의 넘버링 전략을 따라감 -> oracle: sequence, mySql: auto_increment
	private int id; //sequence
	
	@Column(nullable = false, length = 30)
	private String username; //id
	
	@Column(nullable = false, length = 100) //비밀번호를 hash (암호화)하여 db에 저장할 것이기 때문에
	private String password; //pw
	
	@Column(nullable = false, length = 50)
	private String email; //email
	
	//db에는 role type이 없기 때문에 어노테이션을 통해 알려줌
	//@ColumnDefault("'user'")
	@Enumerated(EnumType.STRING)
	private RoleType role; //Enum을 쓰는 게 좋음. admin, user, manager (managerrrr로 오타를 내서 넣어도 manager의 역할을 할 수 있음. String값이기 때문에. 그러나 enum은 도메인, 즉 범위를 정해줄 수 있음.)
	
	@CreationTimestamp //시간이 자동으로 입력이 됨
	private Timestamp createDate; //가입일
}
