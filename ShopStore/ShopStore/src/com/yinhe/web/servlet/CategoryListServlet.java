package com.yinhe.web.servlet;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.yinhe.bean.Category;
import com.yinhe.service.CategoryService;
import com.yinhe.utils.JedisPoolUtils;
import redis.clients.jedis.Jedis;
public class CategoryListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
	private CategoryService categoryService = new CategoryService();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		this.doPost(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		
		//1.获取jedis对象 连接redis数据库
	      Jedis jedis= JedisPoolUtils.getJedis();
	     String categoryListJSon= jedis.get("categoryListJson");
	      if(categoryListJSon==null){
	    	  //准备数据
	    	  List<Category> categoryList=categoryService.findAllCategory();
	    	//创建gson对象
	 		 Gson gson=new Gson();
			 //将集合转换为json对象
			 categoryListJSon= gson.toJson(categoryList);
			 //将json数据放入jedis缓存中
			 jedis.set("categoryListJson", categoryListJSon);
	      }
	
		 response.getWriter().println(categoryListJSon);
	}

}
