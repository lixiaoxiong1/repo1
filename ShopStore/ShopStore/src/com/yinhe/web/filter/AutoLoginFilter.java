package com.yinhe.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.yinhe.bean.User;
import com.yinhe.dao.UserDao;
import com.yinhe.service.UserService;


public class AutoLoginFilter implements Filter {

    

	
	public void destroy() {
	
	}

	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	
		//强制转换HttpServletRequest对象
	      HttpServletRequest req=(HttpServletRequest) request;
	      User user= (User) req.getSession().getAttribute("user");
	      if(user==null){
	    	  Cookie [] cookies= req.getCookies();
	    	  String userName_Cookie=null;
	    	  String password_Cookie=null;
	    	  if(cookies!=null){
	    		   for(Cookie cookie : cookies){
	    			   if("userName_Cookie".equals(cookie.getName())){
	    				   userName_Cookie=cookie.getValue();
	 	    		   }
	    			   if("password_Cookie".equals(cookie.getName())){
	    				   password_Cookie=cookie.getValue();
	 	    		   }
	    		   } 
	    	  }
	    	  if(userName_Cookie!=null&&password_Cookie!=null){
	    		  UserService userService=new UserService(); 
	    		  User login_user= userService.login(userName_Cookie, password_Cookie);
	    		  req.getSession().setAttribute("user", login_user);
	    	  }
	      }
	      chain.doFilter(request, response);
	}

	
	public void init(FilterConfig fConfig) throws ServletException {
		
	}

}
