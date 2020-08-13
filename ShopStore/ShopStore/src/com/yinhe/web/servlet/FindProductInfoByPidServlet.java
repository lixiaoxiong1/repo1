package com.yinhe.web.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yinhe.bean.Category;
import com.yinhe.bean.Product;
import com.yinhe.service.CategoryService;
import com.yinhe.service.ProductService;


public class FindProductInfoByPidServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private ProductService productService=new ProductService();
	
	private CategoryService categoryService=new CategoryService();
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		  this.doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		   String pid=request.getParameter("pid");
		   Product product= productService.findProductByPid(pid);
		   Category category= categoryService.findCategoryByCid(product.getCid()+"");
		   product.setCategory(category);
		   request.setAttribute("product", product);
		   
		   String pids=pid;
		   //将pid放入cookie中
		   Cookie [] cookies=  request.getCookies();
		   if(cookies!=null){
			   for(Cookie cookie: cookies){
				   if("pids".equals(cookie.getName())){
					   //将值 3-2-8存放
					   pids=cookie.getValue();
					   String [] split= pids.split("-");
					   List<String> aslist=Arrays.asList(split);
					   LinkedList<String> list=new LinkedList<String>(aslist);
					   if(list.contains(pid)){
						   list.remove(pid);
						   list.addFirst(pid);
					   }else{
						   list.addFirst(pid);
					   }
					   StringBuffer sb=new StringBuffer();
					   for(int i=0;i<list.size();i++){
						   sb.append(list.get(i)).append("-");
					   }
					    sb.deleteCharAt(sb.length()-1);
					    pids=sb.toString();
				   }
				
			   }
		   }
		   
		   Cookie  cookie=new Cookie("pids", pids);
		   response.addCookie(cookie);
		   
		   request.getRequestDispatcher("product_info.jsp").forward(request, response);
	}

}
