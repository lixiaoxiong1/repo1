package com.yinhe.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.yinhe.bean.Admin;
import com.yinhe.service.UserService;
public class AdminLoginServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
   
	private UserService userService=new UserService();
	//管理员登录
	public void adminLogin(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		   String userName=request.getParameter("userName");
		   String password=request.getParameter("password");
		   Admin admin= userService.adminLogin(userName, password);
		   if(admin==null){
			   request.setAttribute("message", "用户名或者密码错误");
			   request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
			   return ;
		   }
		   request.getSession().setAttribute("admin", admin);
		   response.sendRedirect(request.getContextPath()+"/admin/home.jsp");
	}

}
