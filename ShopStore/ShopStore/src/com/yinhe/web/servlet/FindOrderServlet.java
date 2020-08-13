package com.yinhe.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yinhe.bean.Orderitem;
import com.yinhe.bean.Orders;
import com.yinhe.service.OrderService;

public class FindOrderServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	private OrderService orderService = new OrderService();

	private int currentPage = 1;

	private int pageSize = 5;

	// 后台 订单管理 订单列表
	public void findOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getParameter("currentPage") != null) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		int start = (currentPage - 1) * pageSize;
		List<Orders> orderList = orderService.findAllOrder(start, pageSize);

		int totalPage = orderService.findOrderTotalPage(pageSize);
		for (Orders order : orderList) {

			ArrayList<Orderitem> orderitemList = orderService.findAllOrderItemByOid(order.getOid());
			order.setItems(orderitemList);
		}
		request.setAttribute("orderList", orderList);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("totalPage", totalPage);
		request.getRequestDispatcher("/admin/order/list.jsp").forward(request, response);
	}

}
