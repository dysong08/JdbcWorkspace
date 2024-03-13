package com.kh.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCTemplate {

	
	
//	JDBC 과정 중 반복적으로 쓰이는 구문들을 각각의 메서드로 정의해둘 클래스
//	"재사용 목적"으로 공통 탬플릿 작업을 진행
//	
//	이 클래스에서는 모든 메서드들이 다 static로 만들 예정.
//	
//	공통적인 부분 취합하기
//	
//	1. DB와 접속된 Connection객체 생성 후 반환시켜주는 메서드
//	
//	
//	
//	
//	
	
	// 1. Connection 객체 생성 메서드
	public static Connection getConnection() {
		
		// Connection 객체를 담을 그릇 생성
		
		Connection conn = null;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	

	
	// 2. 전달 받은 JDBC용 객체를 반납시켜주는 메서드(객체별로 오버로딩)
	// 2_1) Connection 객체를 전달받아서 반납시켜주는 메서드
	public static void close(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	// 2_2) Statement 객체를 전달받아서 반납시켜주는 메서드
	public static void close(Statement stmt) {	
		// Statement + PreparedStatement 둘다 매개변수로 전달가능(다형성)
		try {
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	// 2_3) ResultSet 객체를 전달받아서 반납시켜주는 메서드
	public static void close(ResultSet rset) {
		try {
			rset.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	// 3. 전달받은 Connection객체를 트랜잭션 처리 해주는 메서드
	// 3_1) Commit메서드
	public static void commit(Connection conn) {
		try {
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	// 3_2) Rollback메서드
	public static void rollback(Connection conn) {
		try {
			conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
}










