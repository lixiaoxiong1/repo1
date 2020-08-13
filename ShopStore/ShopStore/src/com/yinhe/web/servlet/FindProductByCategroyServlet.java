package com.yinhe.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yinhe.bean.Category;
import com.yinhe.bean.Product;
import com.yinhe.service.ProductService;


public class FindProductByCategroyServlet extends HttpServlet {
	private ProductService productService=new ProductService();
	private  int pageSize=10;
	private  int currentPage=1;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		  this.doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cid=request.getParameter("cid");
		if(request.getParameter("currentPage")!=null){
			currentPage=Integer.parseInt(request.getParameter("currentPage"));
		}
		int start=(currentPage-1)*pageSize;
		List<Product> productList= productService.findProductByCategory(cid,start,pageSize);
		
		int totalCount=productService.getCount(cid);
	
		int totalPage=totalCount%pageSize>0 ?totalCount/pageSize+1:totalCount/pageSize;
		request.setAttribute("productList", productList);
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("cid", cid);
		
		//ä¯ÀÀ¼ÇÂ¼
		List<Product> historyList=new ArrayList<Product>();
		Cookie [] cookies=request.getCookies();
		if(cookies!=null){
			for(Cookie cookie : cookies){
				if("pids".equals(cookie.getName())){
				   String pids=cookie.getValue();
				    String [] splits=pids.split("-");
				    for (int i = 0; i < splits.length; i++) {
						 String pid=splits[i];
						Product product= productService.findProductByPid(pid);
						historyList.add(product);
					}
				}
			}
		}
		
		request.setAttribute("historyList",historyList);
		request.getRequestDispatcher("product_list.jsp").forward(request, response);
	}

}
