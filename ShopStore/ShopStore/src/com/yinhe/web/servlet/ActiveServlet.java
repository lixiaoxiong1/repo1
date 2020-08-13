package com.yinhe.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yinhe.service.UserService;


public class ActiveServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private UserService userService=new UserService();
	
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String activeCode=request.getParameter("activeCode");
		userService.active(activeCode);
		response.sendRedirect(request.getContextPath()+"/login.jsp");
		
	}

}
