package com.yinhe.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import com.yinhe.bean.Cart;
import com.yinhe.bean.CartItem;
import com.yinhe.bean.Orderitem;
import com.yinhe.bean.Orders;
import com.yinhe.bean.User;
import com.yinhe.service.OrderService;
import com.yinhe.utils.CommonsUtils;
import com.yinhe.utils.PaymentUtil;


public class OrderServlet extends BaseServlet {

	private OrderService orderService=new OrderService();
	
	private int currentPage=1;
	
	private int pageSize=5;
	//提交订单
	public void submitOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		
		//1.判断用户是否存在
		HttpSession session= request.getSession();
		User user=(User) session.getAttribute("user");
		if(user==null){
			response.sendRedirect("login.jsp");
			return;
		}
		
		Orders order=new Orders();
		order.setOid(CommonsUtils.getUUID());
		order.setOrdertime(new Date());
		//订单支付状态 1代表已付款 0代表未付款
		order.setState(0);
		Cart cart =(Cart) session.getAttribute("cart");
		order.setTotal(cart.getTotal());
		order.setUid(user.getUid());
		orderService.addOrder(order);
		
	    ArrayList<Orderitem> orderitemList=new ArrayList<Orderitem>();
		HashMap<String,CartItem> cartItems=cart.getCartItems();
		for(Map.Entry<String, CartItem> entry :cartItems.entrySet()){
			CartItem cartItem=  entry.getValue();
			Orderitem orderItem=new Orderitem();
			orderItem.setOid(order.getOid());
			orderItem.setCount(cartItem.getBuyNum());
			orderItem.setItemid(CommonsUtils.getUUID());
			orderItem.setPid(cartItem.getProduct().getPid());
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setSubtotal(cartItem.getSubTotal());
			orderService.addOrderItem(orderItem);
			orderitemList.add(orderItem);
		}
		order.setItems(orderitemList);
		request.setAttribute("order", order);
		request.getRequestDispatcher("../order_info.jsp").forward(request, response);
	
	}
	//确认订单
	public void confirmSubmitOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		
		  Map<String,String[]> properties=  request.getParameterMap();
		  Orders order=new Orders();
		  try {
			BeanUtils.populate(order, properties);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		orderService.updateOrder(order);
		
		//只接入一个接口，这个接口已经集成所有的银行接口了  ，这个接口是第三方支付平台提供的
		//接入的是易宝支付
		// 获得 支付必须基本数据
		String orderid = request.getParameter("oid");
		//String money = order.getTotal()+"";//支付金额
		String money = "0.01";//支付金额
		// 银行
		String pd_FrpId = request.getParameter("pd_FrpId");

		// 发给支付公司需要哪些数据
		String p0_Cmd = "Buy";
		String p1_MerId = ResourceBundle.getBundle("merchantInfo").getString("p1_MerId");
		String p2_Order = orderid;
		String p3_Amt = money;
		String p4_Cur = "CNY";
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";
		// 支付成功回调地址 ---- 第三方支付公司会访问、用户访问
		// 第三方支付可以访问网址
		String p8_Url = ResourceBundle.getBundle("merchantInfo").getString("callback");
		String p9_SAF = "";
		String pa_MP = "";
		String pr_NeedResponse = "1";
		// 加密hmac 需要密钥
		String keyValue = ResourceBundle.getBundle("merchantInfo").getString(
				"keyValue");
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
				p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
				pd_FrpId, pr_NeedResponse, keyValue);


		String url = "https://www.yeepay.com/app-merchant-proxy/node?pd_FrpId="+pd_FrpId+
				"&p0_Cmd="+p0_Cmd+
				"&p1_MerId="+p1_MerId+
				"&p2_Order="+p2_Order+
				"&p3_Amt="+p3_Amt+
				"&p4_Cur="+p4_Cur+
				"&p5_Pid="+p5_Pid+
				"&p6_Pcat="+p6_Pcat+
				"&p7_Pdesc="+p7_Pdesc+
				"&p8_Url="+p8_Url+
				"&p9_SAF="+p9_SAF+
				"&pa_MP="+pa_MP+
				"&pr_NeedResponse="+pr_NeedResponse+
				"&hmac="+hmac;

		//重定向到第三方支付平台
		response.sendRedirect(url);
		
	}
	//我的订单
	public void myOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		
		//1.判断用户是否存在
		HttpSession session= request.getSession();
		User user=(User) session.getAttribute("user");
		if(user==null){
			response.sendRedirect("login.jsp");
			return;
		}
		if(request.getParameter("currentPage")!=null){
			currentPage=Integer.parseInt(request.getParameter("currentPage"));
		}
		int start=(currentPage-1)*pageSize;
		List<Orders> orderList=orderService.findOrderByUid(user.getUid(),start,pageSize);
		int totalCount=orderService.findOrderCountByUid(user.getUid());
		int totalPage=totalCount%pageSize>0 ?totalCount/pageSize+1:totalCount/pageSize;
		for(Orders order : orderList){
			
			ArrayList<Orderitem> orderitemList=orderService.findAllOrderItemByOid(order.getOid());
			order.setItems(orderitemList);
		}
		request.setAttribute("orderList", orderList);
		request.setAttribute("currentPage", currentPage);
	    request.setAttribute("totalPage", totalPage);
		request.getRequestDispatcher("../order_list.jsp").forward(request, response);
		
	}
	
	

}
