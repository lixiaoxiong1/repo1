package com.yinhe.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yinhe.bean.Category;
import com.yinhe.bean.Product;
import com.yinhe.service.CategoryService;
import com.yinhe.service.ProductService;

public class IndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ProductService productService = new ProductService();
	//private CategoryService categoryService = new CategoryService();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 热门商品
		List<Product> hostProductList = productService.findHostProductList();
		// 最新商品
		List<Product> newProductList = productService.findNewProductList();
		//List<Category> categoryList = categoryService.findAllCategory();
		request.setAttribute("hostProductList", hostProductList);
		request.setAttribute("newProductList", newProductList);
		//request.setAttribute("categoryList", categoryList);
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}

}
