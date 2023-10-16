package com.cos.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
public class Reply {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false, length = 200)
	private String content;
	
	//board와 연관관계
	//하나의 게시글에 여러개의 댓글이 달릴 수 있음
	@ManyToOne
	@JoinColumn(name="boardId")
	private Board board;
	
	//댓글 단 사람을 알아야 함
	//한명의 사용자는 여러개의 답변을 달 수 있음. 하나의 답변은 한명의 사용자가 달 수 있음
	@ManyToOne
	@JoinColumn(name="userId")
	private UserInfo userinfo;
	
	@CreationTimestamp
	private Timestamp createDate;

}
