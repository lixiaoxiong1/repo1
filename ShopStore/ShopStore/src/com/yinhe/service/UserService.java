package com.yinhe.service;
import java.sql.SQLException;

import com.yinhe.bean.Admin;
import com.yinhe.bean.User;
import com.yinhe.dao.UserDao;
import com.yinhe.web.servlet.ConfigServlet;
public class UserService {

	private UserDao userDao;
	
	public UserService(){
		userDao=(UserDao) ConfigServlet.getMap().get("userDao");
	}
	  //添加用户
	public boolean  addUser(User user){
		int update=userDao.addUser(user);
		
		return update>0?true:false;
	}
	
	//激活用户
	public void active(String code){
		userDao.active(code);
	}
	
	//校验用户名是否存在
	public boolean isExitUserName(String userName){
		try {
			Long count= userDao.isExitUserName(userName);
			return count>0? true:false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	//登录
	public User login(String userName,String password) {
		try {
			return userDao.login(userName, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//管理员登录
	public Admin adminLogin(String userName,String password){
		try {
			return userDao.adminLogin(userName, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
