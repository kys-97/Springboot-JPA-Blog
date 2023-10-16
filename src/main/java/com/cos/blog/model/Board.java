package com.cos.blog.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder //빌더패턴
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //sequence
	private int id;
	
	@Column(nullable = false, length = 100)
	private String title;
	
	@Lob //대용량 데이터
	private String content; //섬머노트 라이브러리 사용 -> 글이 디자인 되어서 들어갈 때 <html> 태그가 섞여서 디자인 되어 용량이 커짐.
	
	@ColumnDefault("0")
	private int count; //조회수
	
	@ManyToOne(fetch = FetchType.EAGER) //board = many, user = one
	//fetch = FetchType.EAGER: 보드 테이블을 셀렉트 할 때 USER의 정보를 바로 조인해서 가지고 오겠다는 의미
	//ManyToOne이 기본 조건이기 때문
	@JoinColumn(name="userId")
	//작성자 -> foreign key로 받는 게 아니라 자바에서는 오브젝트를 저장할 수 있음. 따라서 fk 사용하지 않고 UserInfo 오브젝트 사용
	private UserInfo userinfo;
	//userinfo 객체를 참조해 자동으로 userid 컬럼 생성됨
	
	//댓글
	//하나의 게시글에는 여러명의 유저가 댓글을 달 수 있음.
	//따라서 collection의 리스트를 사용해야 함
	//보드 테이블을 셀렉트할 때 필요하면 가져오고 필요하면 가져오지 않음 ->fetch = FetchType.LAZY
	//OneToMany의 경우엔 기본적으로 LAZY 전략을 따르지만, 이 경우에는 EAGER 전략을 사용해야 함
	@OneToMany(mappedBy = "board", fetch = FetchType.EAGER) //mappedBy : 연관관계의 주인이 아니다. (FK가 아니라는 뜻) 즉, REPLY 테이블에 외래키로 설정된 BOARD가 연관관계의 주인이라는 의미. 따라서 fk키를 테이블에 만들지 말라는 뜻.
	//mappedBy 뒤의 "board"는 field의 값 -> reply 모델 클래스에 있는 private Board board; 의 필드값
	//@JoinColumn(name="replyId") 이 외래키는 필요하지 않음
	private List<Reply> reply;
	
	@CreationTimestamp
	private Timestamp createDate;
}
