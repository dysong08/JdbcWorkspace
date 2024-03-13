package com.kh.controller;

import java.util.ArrayList;

import com.kh.model.service.MemberService;
import com.kh.model.vo.Member;
import com.kh.view.MemberView;

//Controller : View를 통해서 들어온 요청을 담당
//				해당 메서드로 전달된 데이터들을 가공처리 한 후 Service메서드 호출시 전달한다.
//				Service로부터 반환받은 결과에 따라 사용자가 보게될 화면을 지정한다.


public class MemberController {
	
	
	/**
	 * 사용자의 회원 추가요청을 처리해주는 메서드
	 * @param userId
	 * @param userPwd
	 * @param userName
	 * @param gender
	 * @param age
	 * @param email
	 * @param phone
	 * @param address
	 * @param hobby
	 */
	public void insertMember(String userId, String userPwd, String userName, String gender,
			int age, String email, String phone, String address, String hobby) {
		
		
		// 1. 전달받은 데이터들을 가지고 가공처리하기 => Member 객체로 변환
		//Member m = new Member(userId, userPwd, userName, gender, age, email, phone, address, hobby);
		
		// 빌더
		Member m = new Member.Builder(0)
				.setuserID(userId)
				.userPwd(userPwd)
				.userName(userName)
				.gender(gender)
				.age(age)
				.email(email)
				.phone(phone)
				.address(address)
				.hobby(hobby)
				.build();
		
		// 2. dao의 insertMember 메서드 호출
		int result = new MemberService().insertMember(m);
		
		
		// 3. 결과값이 1인 경우 성공, 0인 경우 실패
		if(result > 0) {
			// 성공
			System.out.println("회원 추가 성공");
		}else { // 실패
			System.out.println("회원 추가 실패");
		}
	}

	
	
	
	public void selectAll() {
		// SELECT * FROM MEMBER;
		// SELECT -> ResultSet -> ArrayList<Member>
		
		ArrayList<Member> list = new MemberService().selectAll();
		
		// 조회 결과가 있는지 없는지 판단 후 사용자가 보게될 화면을 지정
		
		if(list.isEmpty()) {
			// 조회된 결과가 없을 경우
			new MemberView().displayNodata("전체 조회 결과가 없습니다");
		}else {
			// 조회가 되었을 경우
			new MemberView().displayData(list);
		}
		
	}




	public void selectByUserId(String userId) {
		// SELECT
		// SELECT * FROM MEMBER WHERE USERID = '입력한 값'
		// RuseltSet 으로 반환 -> Member
		Member m = new MemberService().selectByUserId(userId);
		
		// 조회 결과가 있는지 없는지 판단 후 사용자가 보게될 view 지정
		if(m == null) {
			// 조회 결과가 없는 상태
			new MemberView().displayNodata(userId + "에 해당하는 검색 결과가 없습니다.");
		}else {
			// 조회 결과가 있는 상태
			new MemberView().displayOne(m);
			
		}
	}




	public void selectByUserName(String keyword) {
		
		ArrayList<Member> list = new MemberService().selectByUserName(keyword);
		
		if (list.isEmpty()) {
			// 검색결과가 없을때 
			// displayNodata 호출
			new MemberView().displayNodata(keyword + "에 해당하는 검색 결과가 없습니다.");
			
			
		}else {
			
			// 검색결과가 있을때
			// displayData 호출
			new MemberView().displayData(list);
						
		}
	}




	public void updateMember(String userId, String newPwd, String newEmail, String newPhone, String newAddress) {
		// VO객체에 입력받은 값들 담기
		Member m = new Member.Builder(0)
				.setuserID(userId)
				.userPwd(newPwd)
				.email(newEmail)
				.phone(newPhone)
				.address(newAddress)
				.build();
		
		// 가공 VO 객체 m을 DAO에 넘기기
		int result = new MemberService().updateMember(m);
		// result : 조회된 행의 갯수 반환
		
		if(result > 0 ) {
			System.out.println("회원정보 변경 성공");
		}else {
			System.out.println("회원정보 변경 실패");
		}
		
	}




	/**
	 * 사용자가 회원 탈퇴 요청시 처리해주는 메서드
	 * @param userId -> 사용자가 입력한 회원의 아이디값
	 */
	public void deleteMember(String userId) {
		
		int result = new MemberService().deleteMember(userId);
		
		if(result > 0) {
			// 회원탈퇴 성공시 탈퇴 성공 메세지 출력 
			System.out.println("회원 탈퇴 성공");

			
		}else {
			// 회원탈퇴 실패시 탈퇴 실패 메세지 출력
			System.out.println("회원 탈퇴 실패");
						
		}
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
}
