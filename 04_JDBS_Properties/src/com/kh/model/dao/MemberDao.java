package com.kh.model.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import com.kh.common.JDBCTemplate;
import com.kh.model.vo.Member;

//	DAO(Date Access Object)
//	Controller에게 요청받은 실질적인 기능을 수행하기 위해서 db에 직접 접근 후
//	sql문을 실행하고 결과 값 돌려받음

public class MemberDao {
	/*
	* 기존 방식 : DAO클래스에 사용자가 요청할 때마다 실행해야 하는 SQL문을
				자바소스코드 내에 직접 명시적으로 작성함 -> 정적코딩방식(하드코딩)
				
	- 문제점 : SQL문을 수정해야할 경우 자바 소스코드를 수정하는 셈으로
			수정된 내용을 반영시키고자 한다면 프로그램을 재구동 해야한다.
			
	- 해결법 : SQL문을 별도로 관리하는 외부파일(.XML)로 만들어서
			실시간으로 이 파일에 기록된 SQL문을 동적으로 읽어들여서 실행 -> 동적코딩방식
	
	*/
	
	/*
	* JDBC용 객체
	- Connection : DB에 연결 정보를 담고 있는 객체(IP주소, PORT번호, 계정명, 비밀번호)
	- (Prepared)Statement : 해당 DB에 SQL문을 전달하고 실행한 후 결과를 받아내는 객체
	- ResultSet : 내가 실행한 SQL문이 SELECT문일 경우 조회된 결과를 담아주는 객체
	
	
	** Statement : 완성된 SQL문을 실행할 수 있는 객체
	
	** PreparedStatement 특징 : SQL문을 바로 실행하지 않고 잠시 보관하는 개념
							미완성된 SQL문을 먼저 전달하고 실행하기전에 완성시킨 형태로
							만든 후 실행만 한다.
	
	미완성된 SQL문 만들기 => 사용자가 입력한 값들이 들어갈 수 있는 공간을 ?(위치홀더)로 확보
						각 위치홀더에 맞는 값들을 셋팅
	
	* 차이점
	1) Statement는 완성된 SQL문을 사용, PreparedStatement는 미완성된 SQL문을 사용한다
	2) Statement객체 생성시 stmt = conn.createStatement();
	   PreparedStatement객체 생성시 pstmt = comm.perpareStatemtment(sql);
	3) Statement로 SQL문 실행시 결과 = stmt.executeXXX(sql);
	   PreparedStatement로 SQL문 실행시 => ?로 표현된 빈 공간을 실제값으로 채워준 후 실행
			   				pstmt.setXXX(?의 위치, 실제값);
	*/
	
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
	
	
	

	private Properties prop = new Properties();
	
	public MemberDao() {
		// 매개변수 없는 기본생성자가 호출될 때마다 prop객체로 데이터를 읽어오는 행위를 정의
		try {
			prop.loadFromXML(new FileInputStream("resources/query.xml"));
			
		} catch (InvalidPropertiesFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 사용자가 회원 추가 요청시 입력했던 값을 가지고 insert문을 실행하는 메서드
	 * @param m : 사용자가 입력했던 아이디~취미까지의 값이 담겨있는 Member객체
	 * @return : insert문으로 실행한 결과 처리된 행의 갯수
	 */
	public int insertMember(Connection conn, Member m) {
		// INSERT문, 처리된 행의 갯수, 트랜잭션처리
		
		// 0) 필요한 변수들 셋팅
		int result = 0;		// 처리된 결과를 담아줄 변수(처리된 행의 갯수)
		//Connection conn = null;		// 접속된 DB의 연결정보를 담는 변수
		PreparedStatement stmt = null;		// SQL문 실행 후 결과를 받기 위한 변수
		
		// 실행할 SQL문은 미완성된 형태로 작성.
		String sql = prop.getProperty("insertMember");
		
		
		// 1) JDBC드라이버 등록
		try {
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			// 오타 or ojdbc6.jar이 누락된 경우 에러발생할 수 있다.
			
			// 2) Connection 객체 생성
			//conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe",
			//		"JDBC", "JDBC");
			
			// 3) PreparedStatement 객체 생성 (SQL문을 미리 넘겨준다)
			//--stmt = conn.createStatement();
			stmt = conn.prepareStatement(sql);
			
			// 3_2) 미완성된 SQL문일 경우 완성시켜주기
			//		stmt.setXXX(?의 위치, 실제값)
			stmt.setString(1, m.getUserId());
			stmt.setString(2, m.getUserPwd());
			stmt.setString(3, m.getUserName());
			stmt.setString(4, m.getGender());
			stmt.setInt(5, m.getAge());
			stmt.setString(6, m.getEmail());
			stmt.setString(7, m.getPhone());
			stmt.setString(8, m.getAddress());
			stmt.setString(9, m.getHobby());
			
			// PreparedStatement의 단점 : 완성된 SQL문을 미리 볼 수 없다.
			
			
			// 4,5) DB에 SQL문을 전달하면서 실행 후 결과 받기
			//--result = stmt.executeUpdate(sql);
			result = stmt.executeUpdate();
			
			// 6) 트랜잭션 처리하기
//			if(result > 0) {	// 1개 이상의 행이 insert됨 -> 성공적으로 1생이 삽입됨 => 커밋하기
//				conn.commit();
//			}else {		// 실패시
//				conn.rollback();
//			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
			// 7) 다쓴 JDBC용 객체 반납 -> 생성된 순서의 역순으로 반납하기
//			try {
			JDBCTemplate.close(stmt);
//				stmt.close();
//				conn.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		
		// 8) 결과 반환
		return result;
	}

	
	
	
	/**
	 * 사용자가 회우너 전체조회 요청시 select문을 실행해주는 메서드
	 * @param conn 
	 * @return
	 */
	public ArrayList<Member> selectAll(Connection conn) {
		
		// 0) 필요한 변수들 셋팅
		// 조회한 결과를 담아줄 변수 => ArrayList
		ArrayList<Member> list = new ArrayList();	// 텅빈 리스트
		
		// Connection, Statement, ResultSet 필요함
		PreparedStatement stmt = null;	// sql문 실행 후 결과를 받기 위한 변수
		ResultSet rset = null;	// SELECT문이 실행된 조회결과값들이 담겨질 객체
		// finally에서 자원반납하기 위해 try전에 셋팅
		
		
		String sql = "SELECT * FROM MEMBER";
		
		try {
			
			// 3) Statement 객체 생성
			stmt = conn.prepareStatement(sql);
			
			// 4)5) sql문 전달 후 실행결과 받기
			rset = stmt.executeQuery();
			
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
			
		}  catch (SQLException e) {
			e.printStackTrace();
		}finally {
			
			// 7) 다쓴 jdbc 객체 반납(생성된 순서의 역순으로)
			JDBCTemplate.close(rset);
			JDBCTemplate.close(stmt);
		}
		
		// 8) 결과 반환(조회 결과들이 뽑혀서 담겨있는 list)
		return list;
	}




	public Member selectByUserId(Connection conn, String userId) {
		// 0) 필요한 변수들 셋팅
		// 조회된 한 회원에 대한 정보를 담을 변수 생성
		Member m = null;
		
		// jdbc 객체 선언
		PreparedStatement stmt = null;
		ResultSet rset = null;
		
		// 실행할 sql문(미완성된 형태로 제시)
		String sql = "SELECT * FROM MEMBER WHERE USERID = ?";
		
		
		try {
			// 3_1) Statement 객체 생성
			stmt = conn.prepareStatement(sql);
			
			// 3_2) SQL문 완성시키기
			stmt.setString(1, userId);
		
			// 4)5) sql문 전달 후 실행결과 받기
			rset = stmt.executeQuery();
		
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
		
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 7) 다쓴 jdbc 객체 반납(생성된 순서의 역순)

			JDBCTemplate.close(rset);
			JDBCTemplate.close(stmt);
		}
		
		return m;
	}




	public ArrayList<Member> selectByUserName(Connection conn, String keyword) {
		
		// 0) 필요한 변수들 셋팅
		// 조회된 한 회원에 대한 정보를 담을 변수 생성
		ArrayList<Member> list = new ArrayList<>();
		
		// jdbc 객체 선언
		PreparedStatement stmt = null;
		ResultSet rset = null;
		
		// 실행할 sql문(미완성된 형태로 제시)
		// 방법1)
		//String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE ?" ;
		
		// 방법2)
		//String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE '%' || ? || '%'" ;
		
		// 방법3)
		String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE CONCAT(CONCAT('%',?), '%')" ;
		
		
		try {
			// 3) Statement 객체 생성
			stmt = conn.prepareStatement(sql);
			
			// 3_2) SQL문 완성시키기
			// 방법1)
			//stmt.setString(1, "%"+keyword+"%");
			
			// 방법2)
			stmt.setString(1, "%"+keyword+"%");
			
			
			
			// 4)5) sql문 전달 후 실행결과 받기
			rset = stmt.executeQuery();
			
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
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
			// 7) 다쓴 jdbc 객체 반납(생성된 순서의 역순으로)
			JDBCTemplate.close(rset);
			JDBCTemplate.close(stmt);
		}
		
		// 8) 결과 반환(조회 결과들이 뽑혀서 담겨있는 list)
		return list;
	}




	public int updateMember(Connection conn, Member m) {

		// 0) 
		int result = 0;
		
		PreparedStatement stmt = null;
		
		String sql = "UPDATE MEMBER "
					+"SET USERPWD = ?,"
					+ "EMAIL = ?,"
					+ "PHONE = ?,"
					+ "ADDRESS = ?"
					+ " WHERE USERID = ?";
		
		// 1) jdbc드라이버 등록
		try {
			
			// 3_1) Statement 객체 생성
			stmt = conn.prepareStatement(sql);
			
			// 3_2)
			stmt.setString(1, m.getUserPwd());
			stmt.setString(2, m.getEmail());
			stmt.setString(3, m.getPhone());
			stmt.setString(4, m.getAddress());
			stmt.setString(5, m.getUserId());
			
			// 4)5)
			result = stmt.executeUpdate();
			
			
		}  catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			JDBCTemplate.close(stmt);
		}
		return result;
	}




	public int deleteMember(Connection conn, String userId) {

		int result = 0;
		
		PreparedStatement stmt = null;
		
		String sql = "DELETE FROM MEMBER WHERE USERID = ?";
		
		try {
			
			// 3) Statement 객체 생성
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, userId);
			
			result = stmt.executeUpdate();
			
		}  catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(stmt);
		}
		
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}




















