package com.KoreaIT.JAM.dao;

import java.sql.Connection;
import java.util.Map;

import com.KoreaIT.JAM.util.DBUtil;
import com.KoreaIT.JAM.util.SecSql;

public class MemberDao {
	
	private Connection conn;

	public MemberDao(Connection conn) {
		this.conn = conn;
	}

	public boolean isLoginDup(String loginId) {
		
		SecSql sql = new SecSql();
		sql.append("SELECT COUNT(*) > 0");
		sql.append("FROM `member`");
		sql.append("WHERE loginId = ?", loginId);
		
		return DBUtil.selectRowBooleanValue(conn, sql);
	}

	public void doJoin(String loginId, String loginPw, String name) {
		
		SecSql sql = SecSql.from("INSERT INTO `member`");
		sql.append("SET regDate = NOW()");
		sql.append(",updateDate = NOW()");
		sql.append(",name = ?", name);
		sql.append(",loginId = ?", loginId);
		sql.append(",loginPw = ?", loginPw);
		
		DBUtil.insert(conn, sql);
	}

	public Map<String, Object> getMember(String loginId) {
		
		SecSql sql = new SecSql();
		sql.append("SELECT *");
		sql.append("FROM `member`");
		sql.append("WHERE loginId = ?", loginId);
		
		return DBUtil.selectRow(conn, sql);
	}

}
