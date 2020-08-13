package com.yinhe.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.yinhe.bean.User;
import com.yinhe.service.UserService;


public class UserServlet extends BaseServlet {
	
	private UserService userService=new UserService();
	
	 public void login(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException{
		 String userName=request.getParameter("userName");
		 String password=request.getParameter("password");
		 String code=request.getParameter("code");
		 User user= userService.login(userName, password);
		 String sRand= (String) request.getSession().getAttribute("sRand");
		 if(!code.equals(sRand)){
			 request.setAttribute("code_message", "验证码错误！");
			 request.getRequestDispatcher("/login.jsp").forward(request, response);
		      return ;
		 }
		 if(user!=null){
			 //自动登录
			  String autoLogin=  request.getParameter("autoLogin");
			  if("true".equals(autoLogin)){
				  
				  Cookie userName_Cookie=new Cookie("userName_Cookie", userName);
				  userName_Cookie.setMaxAge(60);
				  Cookie password_Cookie=new Cookie("password_Cookie", password);
				  password_Cookie.setMaxAge(60);
			
				  
				  response.addCookie(userName_Cookie);
				  response.addCookie(password_Cookie);
				
			  }
			  request.getSession().setAttribute("user", user);
			  request.getRequestDispatcher("index").forward(request, response);
		 }else{
			 request.setAttribute("message", "用户名或者密码错误！");
			 request.getRequestDispatcher("/login.jsp").forward(request, response);
		 }
	 }
	 
	 public void loginOut(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException{
		 
		 HttpSession session= request.getSession();
		 User user= (User) session.getAttribute("user");
		 if(user!=null){
			 session.removeAttribute("user");
		 }
		 response.sendRedirect("login.jsp");
	 }
}
