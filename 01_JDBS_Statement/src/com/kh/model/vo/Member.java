package com.kh.model.vo;

import java.sql.Date;

//	VO(Value Object)
//	DB테이블의 한 행에 대한 데이터를 기록하기 위한 저장용 객체
//
//> 유사용어
//	DTO(Data Transfer Object)
//	DO(Domain Object)
//	Entity(jpa에서 사용)
//	bean(스프링에서 사용)
//	
//	VO 조건
//	1) 반드시 캡슐화 적용
//	2) 기본생성자 및 매개변수 생성자를 작성해야 한다
//	3) 모든 필드에 대해 getter메서드를 작성해야 한다.(setter는 필수 아님)



public class Member {
	// 필드부 : db테이블의 칼럼 정보와 유사하게 필드를 작성

	
	/*
	 * 기존 객체 생성방식
	 * 1. 생성자를 이용한 객체 생성
	 * 	  : 객체 생성시 매개변수로 데이터를 넣어줘서 객체를 생성했다.
	 * 	 -단점 : 1) 인자들이 많을수록 생성자 또한 많아질 수 있다.
	 * 		  ex) Member경우 로그인용 생성자, 회원가입용 생성자, 로그인 요청시 객체, 마이페이지용 등등..
	 * 		  생성자는 딱 필요로 하는 데이터만 담고있는게 좋으므로 그에 맞는 생성자가 많이 필요하게 된다.
	 * 
	 * 		  	 2) 매개변수의 정보를 설명할 수 없기 때문에 잘못된 위치에 데이터를 추가하게될 위험이 있다.
	 * 		  ex) email 자리에 address가 추가될 수도 있다.
	 * 
	 * 2. 기본생성자 호출 후 setter 함수를 이용한 객체 생성
	 * 		생성자를 이용한 객체생성 방법과 비교시 1. 2) 단점을 커버할 수 있다(매개변수의 정보를 설명가능)
	 * 		단, 코드의 길이가 길어지는 단점이 있다. (객체의 일관성이 깨질 수 있다)
	 * 
	 * 3. 빌더패턴 생성방식 : 위 생성방식의 단점을 보완하는 새로운 디자인 패턴
	 * 	(생성자의 인자가 많이 있을때 사용하는 것을 고려한다.)
	 * */
	
	
	
	
//	CREATE TABLE MEMBER (
//		    USERNO NUMBER PRIMARY KEY,
	private int userNo;
	private String userId;
//		    USERID VARCHAR2(15) UNIQUE NOT NULL,
	private String userPwd;
//		    USERPWD VARCHAR2(20) NOT NULL,
	private String userName;
//		    USERNAME VARCHAR2(20) NOT NULL,
	private String gender;
//		    GENDER CHAR(1) CHECK(GENDER IN ('M', 'F')),
	private int age;
//		    AGE NUMBER,
	private String email;
//		    EMAIL VARCHAR2(30),
	private String phone;
//		    PHONE CHAR(11),
	private String address;
//		    ADDRESS VARCHAR2(100),
	private String hobby;
//		    HOBBY VARCHAR2(50),
	private Date enrollDate;	// java.sql로 임포트해야함
//		    ENROLLDATE DATE DEFAULT SYSDATE NOT NULL
//		);
//		DROP SEQUENCE SEQ_USERNO;
//		CREATE SEQUENCE SEQ_USERNO
//		NOCACHE;

	
	
	public Member() {
	}

	





	// 회원 추가용 생성자 => userNo, enrollDate 빼고 모든 필드를 초기화
//	public Member(String userId, String userPwd, String userName, String gender, int age, String email, String phone, String address,
//			String hobby) {
//		super();
//		this.userId = userId;
//		this.userPwd = userPwd;
//		this.userName = userName;
//		this.gender = gender;
//		this.age = age;
//		this.email = email;
//		this.phone = phone;
//		this.address = address;
//		this.hobby = hobby;
//	}
//
//	public Member(int userNo, String userId, String userPwd, String userName, String gender, int age, String email, String phone,
//			String address, String hobby, Date enrollDate) {
//		super();
//		this.userNo = userNo;
//		this.userId = userId;
//		this.userPwd = userPwd;
//		this.userName = userName;
//		this.gender = gender;
//		this.age = age;
//		this.email = email;
//		this.phone = phone;
//		this.address = address;
//		this.hobby = hobby;
//		this.enrollDate = enrollDate;
//	}
	
	
	// 클래스 내부에 빌더라는 이름의 클래스 생성(이너클래스)
	/*
	 * 빌더 이용시 장점
	 * 1. 불필요한 생성자 제거
	 * 2. 데이터의 순서와 상관없이 객체 생성이 가능하다
	 * 3. 명시적으로 메서드명을 선언하여 가독성이 좋다
	 * 4. 각 인자가 어떤 데이터인지 즉시 확인 가능하다
	 * 5. setter 함수를 만들지 않으므로 객체일관성이 유지된다.
	 * */
	public static class Builder {
		private int userNo;
		private String userId;
		private String userPwd;
		private String userName;
		private String gender;
		private int age;
		private String email;
		private String phone;
		private String address;
		private String hobby;
		private Date enrollDate;
		
		// 필수변수는 생성자에 값을 넣어줌
		public Builder(int userNo) {
			this.userNo = userNo;
		}
		
		// 필드별로 메서드 만든 후 반환값으로 빌더객체를 리턴
		public Builder setuserID(String userId) {
			this.userId = userId;
			
			return this;
			// 현재 빌더객체를 반환. 메서드 체이닝이 가능
		}
		
		public Builder userPwd(String userPwd) {
			this.userPwd = userPwd;
			
			return this;
		}
		
		public Builder userName(String userName) {
			this.userName = userName;
			
			return this;
		}
		
		public Builder gender(String gender) {
			this.gender = gender;
			
			return this;
		}
		
		public Builder age(int age) {
			this.age = age;
			
			return this;
		}
		public Builder email(String email) {
			this.email = email;
			
			return this;
		}
		
		public Builder phone(String phone) {
			this.phone = phone;
			
			return this;
		}
		
		public Builder address(String address) {
			this.address = address;
			
			return this;
		}
		
		public Builder hobby(String hobby) {
			this.hobby = hobby;
			
			return this;
		}
		public Builder enrollDate(Date enrollDate) {
			this.enrollDate = enrollDate;
			
			return this;
		}
		
		// 빌더 메서드 
		public Member build() {
			Member m = new Member();
			m.userNo = userNo;
			m.userId = userId;
			m.userPwd = userPwd;
			m.userName = userName;
			m.gender = gender;
			m.age = age;
			m.email = email;
			m.phone = phone;
			m.address = address;
			m.hobby = hobby;
			m.enrollDate = enrollDate;
			
			return m;
		}
		
	}
	
	
	
	
	// setter함수도 지워줘야 하지만 수정할 부분이 많아지므로 주석처리하지 않음
	public int getUserNo() {
		return userNo;
	}

	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public Date getEnrollDate() {
		return enrollDate;
	}

	public void setEnrollDate(Date enrollDate) {
		this.enrollDate = enrollDate;
	}


	@Override
	public String toString() {
		return "Member [userNo=" + userNo + ", userId=" + userId + ", userPwd=" + userPwd + ", userName=" + userName
				+ ", gender=" + gender + ", age=" + age + ", email=" + email + ", phone=" + phone + ", address="
				+ address + ", hobby=" + hobby + ", enrollDate=" + enrollDate + "]";
	}

	
	
}
