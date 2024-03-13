package com.kh.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.kh.model.vo.Member;

//	DAO(Date Access Object)
//	Controller에게 요청받은 실질적인 기능을 수행하기 위해서 db에 직접 접근 후
//	sql문을 실행하고 결과 값 돌려받음

public class MemberDao {

	
//	* JDBC용 객체
//	- Connection : DB에 연결 정보를 담고 있는 객체(IP주소, PORT번호, 계정명, 비밀번호)
//	- (Prepared)Statement : 해당 DB에 SQL문을 전달하고 실행한 후 결과를 받아내는 객체
//	- ResultSet : 내가 실행한 SQL문이 SELECT문일 경우 조회된 결과를 담아주는 객체
//	
//	
//	* JDBC 처리순서
//	1) JDBC Driver 등록 : DBMS가 제공하는 클래스 등록
//	2) Connection 생성 : 접속하고자 하는 DB정보를 입력해서 DB에 접속(접속시 Connection생성)
//	3) Statement 생성 : Connection 객체를 이용해서 생성
//	4) SQL문을 전달하면서 쿼리문 실행 : Statement객체를 이용해서 SQL문 실행
//		> SELECT문일 경우 -> executeQuery() 메서드를 이용해서 실행
//		> 나머지 DML문일 경우 -> executeUpdate() 메서드를 이용해서 실행
//	5) 결과 반환
//		> SELECT문일 경우 -> 결과값은 ResultSet 객체로 받기(조회된 데이터들이 담겨 있음)
//		> 나머지 DML문일 경우 -> int형 변수로 받기(처리된 행의 갯수가 담겨있음)
//	6) ResultSet객체에 담긴 데이터들을 하나씩 추출하여 VO객체로 변환
//		or 트랜잭션 처리(SELECT가 아닌 DML문일 경우)
//	7) 다 쓴 JDBC용 객체 자원 반납(생성된 순서의 역순으로)
//	8) 결과를 Controller에 반환
	
	
	
//	** Statement : 완성된 SQL문을 실행할 수 있는 객체
	
	
	
	/**
	 * 사용자가 회원 추가 요청시 입력했던 값을 가지고 insert문을 실행하는 메서드
	 * @param m : 사용자가 입력했던 아이디~취미까지의 값이 담겨있는 Member객체
	 * @return : insert문으로 실행한 결과 처리된 행의 갯수
	 */
	public int insertMember(Member m) {
		// INSERT문, 처리된 행의 갯수, 트랜잭션처리
		
		// 0) 필요한 변수들 셋팅
		int result = 0;		// 처리된 결과를 담아줄 변수(처리된 행의 갯수)
		Connection conn = null;		// 접속된 DB의 연결정보를 담는 변수
		Statement stmt = null;		// SQL문 실행 후 결과를 받기 위한 변수
		
		// 실행할 SQL문 필요(주의점 : 끝에 세미콜론이 있으면 안됨!!)
		String sql = "INSERT INTO MEMBER VALUES("
				+ "SEQ_USERNO.NEXTVAL, "
				+ "'" +m.getUserId() + "',"
				+ "'" +m.getUserPwd() + "',"
				+ "'" +m.getUserName() + "',"
				+ "'" +m.getGender() + "',"
				+ "'" +m.getAge() + "',"
				+ "'" +m.getEmail() + "',"
				+ "'" +m.getPhone() + "',"
				+ "'" +m.getAddress() + "',"
				+ "'" +m.getHobby() + "',"
				+ "DEFAULT"
				+ ")";
		
		
		// 1) JDBC드라이버 등록
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 오타 or ojdbc6.jar이 누락된 경우 에러발생할 수 있다.
			
			// 2) Connection 객체 생성
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe",
					"JDBC", "JDBC");
			
			// 3) Statement 객체 생성
			stmt = conn.createStatement();
			
			// 4,5) DB에 SQL문을 전달하면서 실행 후 결과 받기
			result = stmt.executeUpdate(sql);
			
			// 6) 트랜잭션 처리하기
			if(result > 0) {	// 1개 이상의 행이 insert됨 -> 성공적으로 1생이 삽입됨 => 커밋하기
				conn.commit();
			}else {		// 실패시
				conn.rollback();
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
			// 7) 다쓴 JDBC용 객체 반납 -> 생성된 순서의 역순으로 반납하기
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// 8) 결과 반환
		return result;
	}

	
	
	
	/**
	 * 사용자가 회우너 전체조회 요청시 select문을 실행해주는 메서드
	 * @return
	 */
	public ArrayList<Member> selectAll() {
		
		// 0) 필요한 변수들 셋팅
		// 조회한 결과를 담아줄 변수 => ArrayList
		ArrayList<Member> list = new ArrayList();	// 텅빈 리스트
		
		// Connection, Statement, ResultSet 필요함
		Connection conn = null;	// 접속된 DB의 연결정보를 담는 변수
		Statement stmt = null;	// sql문 실행 후 결과를 받기 위한 변수
		ResultSet rset = null;	// SELECT문이 실행된 조회결과값들이 담겨질 객체
		// finally에서 자원반납하기 위해 try전에 셋팅
		
		
		String sql = "SELECT * FROM MEMBER";
		
		try {
			// 1) jdbc드라이버 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2) Connection 객체 생성
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe",
					"JDBC", "JDBC");
			
			// 3) Statement 객체 생성
			stmt = conn.createStatement();
			
			// 4)5) sql문 전달 후 실행결과 받기
			rset = stmt.executeQuery(sql);
			
			// 6-1) 현재 조회 결과가 담긴 ResultSet에서 한 행씩 뽑아서 VO객체에 담기
			// rset.next();	: 커서를 한줄 아래로 옮겨주고(다음행) 해당 행이 존재할 경우 true / 아니면 false반환
			
			while(rset.next()) {
				// 현재 rset의 커서가 가리키고 있는 행의 데이터가 있다면 반복을 진행.
				
				Member m = new Member();
				/*
				 * 	rset으로부터 어떤 칼럼에 해당하는 값을 뽑을건지 제시해야 함
				 * => 칼럼명, 칼럼순번
				 * => 권장사항 : 칼럼명으로 작성하고 대문자로 쓰는 것을 권장.
				 * 
				 * 	rset.getInt(칼럼명 또는 칼럼순번)
				 * 	rset.getString(칼럼명 또는 칼럼순번)
				 * 	rset.getDate(칼럼명 또는 칼럼순번)
				 * */
				
				m.setUserNo(rset.getInt("USERNO"));
				m.setUserId(rset.getString("USERID"));
				m.setUserPwd(rset.getString("USERPWD"));
				m.setUserName(rset.getString("USERNAME"));
				m.setGender(rset.getString("gender"));	// 대소문자 구분하지 않음
				m.setAge(rset.getInt("age"));
				m.setEmail(rset.getString("email"));
				m.setPhone(rset.getString(8));	// 칼럼의 순번 제시가능
				m.setAddress(rset.getString(9));
				m.setHobby(rset.getString(10));
				m.setEnrollDate(rset.getDate(11));
				// 한 행에 대한 모든 칼럼의 데이터값들을
				// 각각의 필드에 담아 하나의 Member 객체에 옮겨담기 끝
				// 리스트에 해당 Member객체를 담아주면 됨
				
				list.add(m);
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
			// 7) 다쓴 jdbc 객체 반납(생성된 순서의 역순으로)
			try {
				rset.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		// 8) 결과 반환(조회 결과들이 뽑혀서 담겨있는 list)
		return list;
	}




	public Member selectByUserId(String userId) {
		// 0) 필요한 변수들 셋팅
		// 조회된 한 회원에 대한 정보를 담을 변수 생성
		Member m = null;
		
		// jdbc 객체 선언
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		
		// 실행할 sql문(완성된 형태로 제시)
		String sql = "SELECT * FROM MEMBER WHERE USERID = '"+userId +"'" ;
		
		
		try {
			// 1) jdbc 드라이버 등록하기
			Class.forName("oracle.jdbc.driver.OracleDriver");
		
		
			// 2) Connection 객체 생성
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe",
				"JDBC", "JDBC");
		
			// 3) Statement 객체 생성
			stmt = conn.createStatement();
		
			// 4)5) sql문 전달 후 실행결과 받기
			rset = stmt.executeQuery(sql);
		
			// 6) 현재 조회결과가 담긴 ResultSet에서 한행씩 뽑아 VO객체에 담기
			if(rset.next()) {
				// 조회된 행의 데이터를 뽑아 Member객체에 담아주기
				m = new Member();
				
				m.setUserNo(rset.getInt("USERNO"));
				m.setUserId(rset.getString("USERID"));
				m.setUserPwd(rset.getString("USERPWD"));
				m.setUserName(rset.getString("USERNAME"));
				m.setGender(rset.getString("gender"));	// 대소문자 구분하지 않음
				m.setAge(rset.getInt("age"));
				m.setEmail(rset.getString("email"));
				m.setPhone(rset.getString(8));	// 칼럼의 순번 제시가능
				m.setAddress(rset.getString(9));
				m.setHobby(rset.getString(10));
				m.setEnrollDate(rset.getDate(11));
				
			}else {
				
			}
		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 7) 다쓴 jdbc 객체 반납(생성된 순서의 역순)
			try {
				rset.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return m;
	}




	public ArrayList<Member> selectByUserName(String keyword) {
		
		// 0) 필요한 변수들 셋팅
		// 조회된 한 회원에 대한 정보를 담을 변수 생성
		ArrayList<Member> list = new ArrayList<>();
		
		// jdbc 객체 선언
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		
		// 실행할 sql문(완성된 형태로 제시)
		String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE '%"+keyword+"%'"  ;
		
		try {
			// 1) jdbc드라이버 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2) Connection 객체 생성
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe",
					"JDBC", "JDBC");
			
			// 3) Statement 객체 생성
			stmt = conn.createStatement();
			
			// 4)5) sql문 전달 후 실행결과 받기
			rset = stmt.executeQuery(sql);
			
			// 6-1) 현재 조회 결과가 담긴 ResultSet에서 한 행씩 뽑아서 VO객체에 담기
			// rset.next();	: 커서를 한줄 아래로 옮겨주고(다음행) 해당 행이 존재할 경우 true / 아니면 false반환
			
			while(rset.next()) {
				// 현재 rset의 커서가 가리키고 있는 행의 데이터가 있다면 반복을 진행.
				
				Member m = new Member();
				/*
				 * 	rset으로부터 어떤 칼럼에 해당하는 값을 뽑을건지 제시해야 함
				 * => 칼럼명, 칼럼순번
				 * => 권장사항 : 칼럼명으로 작성하고 대문자로 쓰는 것을 권장.
				 * 
				 * 	rset.getInt(칼럼명 또는 칼럼순번)
				 * 	rset.getString(칼럼명 또는 칼럼순번)
				 * 	rset.getDate(칼럼명 또는 칼럼순번)
				 * */
				
				m.setUserNo(rset.getInt("USERNO"));
				m.setUserId(rset.getString("USERID"));
				m.setUserPwd(rset.getString("USERPWD"));
				m.setUserName(rset.getString("USERNAME"));
				m.setGender(rset.getString("gender"));	// 대소문자 구분하지 않음
				m.setAge(rset.getInt("age"));
				m.setEmail(rset.getString("email"));
				m.setPhone(rset.getString(8));	// 칼럼의 순번 제시가능
				m.setAddress(rset.getString(9));
				m.setHobby(rset.getString(10));
				m.setEnrollDate(rset.getDate(11));
				// 한 행에 대한 모든 칼럼의 데이터값들을
				// 각각의 필드에 담아 하나의 Member 객체에 옮겨담기 끝
				// 리스트에 해당 Member객체를 담아주면 됨
				
				list.add(m);
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
			// 7) 다쓴 jdbc 객체 반납(생성된 순서의 역순으로)
			try {
				rset.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		// 8) 결과 반환(조회 결과들이 뽑혀서 담겨있는 list)
		return list;
	}




	public int updateMember(Member m) {

		// 0) 
		int result = 0;
		
		Connection conn = null;
		Statement stmt = null;
		
		String sql = "UPDATE MEMBER"
					+" SET USERPWD = '"+ m.getUserPwd() + "',"
					+ "EMAIL = '" + m.getEmail() + "',"
					+ "PHONE = '" + m.getPhone() + "',"
					+ "ADDRESS = '" + m.getAddress() + "'"
					+ " WHERE USERID = '" + m.getUserId()+ "'";
		
		// 1) jdbc드라이버 등록
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2) Connection 객체 생성
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe",
					"JDBC", "JDBC");
			
			// 3) Statement 객체 생성
			stmt = conn.createStatement();
			
			// 4)5)
			result = stmt.executeUpdate(sql);
			
			// 6)
			if(result > 0) {
				conn.commit();
			}else {
				conn.rollback();
				
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			try {
				stmt.close();
				conn.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}




	public int deleteMember(String userId) {

		int result = 0;
		
		Connection conn = null;
		Statement stmt = null;
		
		String sql = "DELTE FROM MEMBER WHERE USERID = '"+ userId + "'";
		
		try {
			// 1) 
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2) Connection 객체 생성
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe",
					"JDBC", "JDBC");
			
			// 3) Statement 객체 생성
			stmt = conn.createStatement();
			
			result = stmt.executeUpdate(sql);
			
			if(result > 0) {
				conn.commit();
			}else {
				conn.rollback();
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}




















