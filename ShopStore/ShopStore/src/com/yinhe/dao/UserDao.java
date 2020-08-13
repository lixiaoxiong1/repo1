package com.yinhe.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.yinhe.bean.Admin;
import com.yinhe.bean.User;
import com.yinhe.utils.DataSourceUtils;

public class UserDao {

	// 添加用户
	public int addUser(User user) {
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "insert into user values(?,?,?,?,?,?,?,?,?,?)";
		try {
			int update = queryRunner.update(sql, user.getUid(), user.getUsername(), user.getPassword(), user.getName(),
					user.getEmail(), user.getTelephone(), user.getBirthday(), user.getSex(), user.getState(),
					user.getCode());
			return update;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	// 激活
	public void active(String code) {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "update user set state=? where code=?";
		try {
			runner.update(sql, 1, code);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//校验用户是否存在
	public Long isExitUserName(String userName) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select count(*) from user where userName=?";
		Long result = (Long) runner.query(sql, new ScalarHandler(), userName);
		return result;
	}
	//登录
	public User login(String userName,String password) throws SQLException{
		 QueryRunner  runner=new QueryRunner(DataSourceUtils.getDataSource());
		 String sql="select * from user where userName=? and password=?";
		return runner.query(sql, new BeanHandler<User>(User.class), userName,password);
	}
	//管理员登录
	public Admin adminLogin(String userName,String password) throws SQLException{
		QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from admin where userName=? and password=?";
	   return runner.query(sql, new BeanHandler<Admin>(Admin.class), userName,password);
	}
	public User findUserByuid(String uid) throws SQLException{
		 QueryRunner  runner=new QueryRunner(DataSourceUtils.getDataSource());
		 String sql="select * from user where uid=?";
		return runner.query(sql, new BeanHandler<User>(User.class), uid);
	}
}
