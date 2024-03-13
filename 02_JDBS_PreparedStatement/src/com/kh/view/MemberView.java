package com.kh.view;

import java.util.List;
import java.util.Scanner;

import com.kh.controller.MemberController;
import com.kh.model.vo.Member;

//	View : 사용자가 보게될 시각적인 요소를 담당(화면 => 입,출력)
			


public class MemberView {
	// 전역으로 쓸 수 있는 Scanner 객체 생성
	private Scanner sc = new Scanner(System.in); 
	
	// MemberController 객체 생성
	private MemberController mc = new MemberController();
	
	
	/**
	 * 사용자가 보게될 화면(메인화면) alt+shift+j
	 */
	public void mainmenu() {
		
		while(true) {
			System.out.println("***** 회원 관리 프로그램 *****");
			System.out.println("1. 회원 추가");
			System.out.println("2. 회원 전체 조회");
			System.out.println("3. 회원 아이디로 검색");
			System.out.println("4. 회원 이름 키워드 검색");
			System.out.println("5. 회원 정보 변경");
			System.out.println("6. 회원 탈퇴");
			System.out.println("0. 프로그램 종료");
			System.out.print("이용할 메뉴 선택 : ");
			int menu = Integer.parseInt(sc.nextLine());
			
			switch(menu) {
			case 1: insertMember(); break;
			case 2: selectAll(); break;
			case 3: selectByUserId(); break;
			case 4: selectByUserName(); break;
			case 5: updateMember(); break;
			case 6: deleteMember(); break;
			case 0: System.out.println("프로그램을 종료합니다."); return;
			default : System.out.println("잘못 선택 했습니다."); 
			}
		}
	}


	


	private void deleteMember() {
		System.out.println("----- 회원 탈퇴 -----");
		
		System.out.print("탈퇴할 회원 아이디 : ");
		String userId = sc.nextLine();
		
		mc.deleteMember(userId);
		
	}





	/**
	 * 사용자에게 변경할 회원의 아이디, 변경할 정보들을 입력받은 후 변경요청하는 메서드
	 */
	private void updateMember() {
		System.out.println("----- 회원 정보 변경 -----");
		
		// 변경할 회원의 아이디
		System.out.println("변경할 회원의 아이디 : ");
		String userId = sc.nextLine();
		
		// 변경할 정보들
		System.out.println("변경할 비밀번호 : ");
		String newPwd = sc.nextLine();
		
		System.out.println("변경할 이메일 : ");
		String newEmail = sc.nextLine();
		
		System.out.println("변경할 휴대폰번호(숫자만) : ");
		String newPhone = sc.nextLine();
		
		System.out.println("변경할 주소 : ");
		String newAddress = sc.nextLine();
		
		
		// 회원정보 수정요청
		mc.updateMember(userId, newPwd, newEmail, newPhone, newAddress);
		
		
		
		
	}





	/**
	 * 사용자의 아이디로 검색요청을 처리해주는 메서드
	 */
	private void selectByUserId() {
		System.out.println("----- 회원 아이디로 검색 -----");
		
		System.out.print("검색할 회원의 아이디 : ");
		String userId = sc.nextLine();
		
		// 입력받은 아이디를 회원 아이디 검색 요청시 같이 넘김
		mc.selectByUserId(userId);
		
		
	}





	/**
	 * 회원 추가용 화면
	 * 추가하고자 하는 회원의 정보를 입력받아서 회원 추가요청 할 수 있는 화면
	 */
	private void insertMember() {
		
		System.out.println("----- 회원 추가 -----");
		
		// 입력 유도문
		System.out.print("아이디 : ");
		String userId = sc.nextLine();
		
		System.out.print("비밀번호 : ");
		String userPwd = sc.nextLine();
		
		System.out.print("이름 : ");
		String userName = sc.nextLine();
		
		System.out.print("성별(M/F) : ");
		String gender = sc.nextLine();
		
		System.out.print("age : ");
		int age = Integer.parseInt(sc.nextLine());
		
		System.out.print("이메일 : ");
		String email = sc.nextLine();
		
		System.out.print("핸드폰번호 : ");
		String phone = sc.nextLine();
		
		System.out.print("주소 : ");
		String address = sc.nextLine();
		
		System.out.print("취미(,로 공백없이 나열) : ");	// ex)운동,게임,영화..
		String hobby = sc.nextLine();
		
		
		// 입력받은 정보를 가지고 회원 추가요청 보내기
		mc.insertMember(userId, userPwd, userName, gender, age, email, phone, address, hobby);
		
	}
	
	
	
	/**
	 * 회원 전체 조회 메서드
	 */
	private void selectAll() {
		
		System.out.println("----- 회원 전체 조회 -----");
		
		// 회원 전체 조회
		mc.selectAll();
	}
	
	//======================================================
	
	// 서비스 요청 처리 후 사용자가 보게된 응답화면들
	
	public void displayData(List<Member> list) {
		System.out.println("\n조회된 데이터는 " + list.size()+ "건 입니다.\n");
		
		for(Member m : list) {
			System.out.println(m);
		}
	}
	
	
	public void displayNodata(String message) {
		System.out.println(message);

	}


	
	public void displayOne(Member m) {
		
		System.out.println("\n조회된 데이터는 다음과 같습니다.");
		
		System.out.println(m);
		
	}



	public void selectByUserName() {
		System.out.println("----- 회원 이름 키워드 검색 -----");
		System.out.print("회원 이름 키워드 입력 : ");
		String keyword = sc.nextLine();
		
		mc.selectByUserName(keyword);
	}



	
}



















