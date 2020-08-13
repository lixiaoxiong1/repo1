package com.yinhe.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yinhe.service.UserService;

public class CheckUsernameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
     private  UserService userService=new UserService();  
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		this.doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		   request.setCharacterEncoding("UTF-8");
		   String userName= request.getParameter("username");
		   System.out.println(userName);
		   boolean isExit=userService.isExitUserName(userName);
		   String json="{isExit:"+isExit+"}";
		   System.out.println(json);
		   response.getWriter().println(json);
	}

}
