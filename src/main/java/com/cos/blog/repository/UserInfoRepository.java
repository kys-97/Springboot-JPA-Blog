package com.cos.blog.repository;

import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.blog.model.UserInfo;

//JpaRepository는 userInfo가 관리하는 repository고, 그 값은 Integer
//@DynamicInsert insert 시 null인 field 제외 -> model class에서 role의 default값 제거하면 해결됨
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer>{
	
	//DAO 라고 생각하면 됨
	//기본적인 CRUD 기능을 가지고 있음
	//자동으로 BEAN 등록이 되기 때문에 Repository 어노테이션을 생략 가능

}
