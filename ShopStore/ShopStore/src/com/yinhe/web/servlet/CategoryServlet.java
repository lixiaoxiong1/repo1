package com.yinhe.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yinhe.bean.Category;
import com.yinhe.service.CategoryService;
import com.yinhe.utils.CommonsUtils;

public class CategoryServlet extends BaseServlet {
	
	private CategoryService categoryService=new CategoryService();
	
	public void categoryList(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		
		List<Category> categoryList=  categoryService.findAllCategory();
	
		request.setAttribute("categoryList", categoryList);
		request.getRequestDispatcher("/admin/category/list.jsp").forward(request, response);
	}
	
	public void addCategory(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		request.setCharacterEncoding("utf-8");
		String id=CommonsUtils.getUUID();
		String cname=request.getParameter("cname");
		categoryService.addCategory(id, cname);
		request.getRequestDispatcher("category?method=categoryList").forward(request, response);
	}
	//删除
	public void deleteCategory(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String cid=request.getParameter("cid");
		categoryService.deleteCategory(cid);
		request.getRequestDispatcher("category?method=categoryList").forward(request, response);
	}
	//跳转更新页面
	public void toUpdateCategory(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String cid=request.getParameter("cid");
	   Category category=	categoryService.findCategoryByCid(cid);
	   request.setAttribute("category", category);
	   request.getRequestDispatcher("/admin/category/edit.jsp").forward(request, response);
	}
	
	//更新
	public void updateCategory(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		String cid=request.getParameter("cid");
		String cname=request.getParameter("cname");
		categoryService.updateCategory(cid, cname);
		request.getRequestDispatcher("category?method=categoryList").forward(request, response);
	}

}
