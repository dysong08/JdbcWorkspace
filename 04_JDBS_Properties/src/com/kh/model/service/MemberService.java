package com.kh.model.service;

import java.sql.Connection;
import java.util.ArrayList;

import com.kh.common.JDBCTemplate;
import com.kh.model.dao.MemberDao;
import com.kh.model.vo.Member;

//
//	* Service : 기존의 DAO의 역할을 분담
//				컨트롤러 -> 서비스 -> DAO
//				(컨트롤러에서 서비스 호출 후 서비스를 거쳐 DAO로 넘어갈 예정)
//				DAO 호출시 커넥션 객체와 기존에 넘기고자 했던 매개변수를 같이 넘겨준다.
//
//				DAO처리가 끝나면 서비스단에서 결과에 따른 트랜잭션 처리도 같이 해줌
//				=> 서비스단을 추가함으로써 DAO에는 순수하게 SQL문을 처리하는 부분만 남게됨


public class MemberService {

	
	
	public int insertMember(Member m) {
		// Connection 객체 생성
		
		Connection conn = JDBCTemplate.getConnection();
		
		// DAO 호출시 Connection객체와 기존에 넘기고자 했던 매개변수를 같이 넘겨준다.
		int result = new MemberDao().insertMember(conn, m);
		
		
		// 결과에 따른 트랜잭션 처리
		if(result > 0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		
		// 다쓴 JDBC 객체 반납
		JDBCTemplate.close(conn);
		
		// 결과반환
		return result;
	}

	
	
	public ArrayList<Member> selectAll() {
		
		// 1) Connection 객체 생성
		Connection conn = JDBCTemplate.getConnection();
		
		
		// 2) 결과값을 받을 변수 선언, DAO호출 후 리턴값 받아주기
		// 이때  Connection객체 전달
		ArrayList<Member> list = new MemberDao().selectAll(conn);
		
		
		// 3) Connection 객체 close
		JDBCTemplate.close(conn);
		
		// 4) 결과값 리턴
		return list;
	}
	
	

	public Member selectByUserId(String userId) {
		
		// 1) Connection 객체 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 2) 결과값을 담을 변수
		Member m = new MemberDao().selectByUserId(conn, userId);
		
		// 3) Connection 객체 close
		JDBCTemplate.close(conn);
		
		return m;
	}

	
	
	
	public ArrayList<Member> selectByUserName(String keyword) {
		
		// 1) Connection 객체 생성
		Connection conn = JDBCTemplate.getConnection();
		
		ArrayList<Member> list = new MemberDao().selectByUserName(conn, keyword);
		
		JDBCTemplate.close(conn);
		
		return list;
	}
	
	
	

	public int updateMember(Member m) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = new MemberDao().updateMember(conn, m);
		
		// 3) 결과값에 따른 트랜잭션 처리 commit or rollback처리
		if(result > 0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		
		// 4) JDBC 객체 자원 반납
		JDBCTemplate.close(conn);
		
		return result;
	}

	
	
	public int deleteMember(String userId) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = new MemberDao().deleteMember(conn, userId);
		
		// 3) 결과값에 따른 트랜잭션 처리 commit or rollback
		if(result > 0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
				
		// 4) JDBC 객체 자원 반납
		JDBCTemplate.close(conn);
		
		return result;
	}

	
	
	
	
	
	
	
	
	
	
	
}
