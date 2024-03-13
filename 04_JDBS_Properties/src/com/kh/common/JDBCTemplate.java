package com.kh.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

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
		/*
		 * 	* 기존방식 : JDBC Driver구문, 내가 접속할 DB의 URL, 계쩡, 비밀번호가
		 * 			자바 소스코드 내에 명시적으로 작성함 -> 정적 코딩방식(하드코딩)
		 * 
		 * - 문제점 : 
		 * 1) DBMS가 변경될 경우, 접속할 URL이나 계정 및 비밀번호가 변경될 경우
		 * 		자바 소스코드를 수정해야 함. 수정된 내용을 반영시키고자 한다면 프로그램을 재구동해야 함
		 * 		=> 사용자 입장에서 사용중이던 어플리케이션이 비정상적으로 종료되었다가 재구동될 수 있다.
		 * 	
		 * 2) 유지보수에 불편함이 있다.
		 * 
		 * 3) 보안상 문제가 될 수도 있다.
		 * 
		 * */
		Properties prop = new Properties();
		
		
		Connection conn = null;
		
		try {
			// prop로부터 loaed메서드를 이용해서 각 키에 해당하는 value값 가져오기
			prop.load(new FileInputStream("resources/driver.properties"));
			
			// prop로부터 getProperty메서드를 이용해서 각 키에 해당하는 value값 추출(동적코딩)
			Class.forName(prop.getProperty("driver"));
			
			
			conn = DriverManager.getConnection(prop.getProperty("url"), 
					prop.getProperty("username"), prop.getProperty("password"));
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
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










