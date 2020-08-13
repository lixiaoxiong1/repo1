package com.yinhe.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.yinhe.bean.Orderitem;
import com.yinhe.bean.Orders;
import com.yinhe.bean.Product;
import com.yinhe.bean.User;
import com.yinhe.dao.OrderDao;
import com.yinhe.dao.UserDao;

public class OrderService {

	private OrderDao orderDao=new OrderDao();
	private UserDao userDao=new UserDao();
	//添加订单
	public void addOrder(Orders order){
		try {
			orderDao.addOrder(order);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//添加订单项
	public void addOrderItem(Orderitem orderItem){
		try {
			orderDao.addOrderItem(orderItem);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//更新订单
	public void updateOrder(Orders order){
		try {
			orderDao.updateOrder(order);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//修改订单支付状态
	public void updateOrderStateByOid(String oid) {
		try {
			orderDao.updateOrderStateByOid(oid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Orders> findOrderByUid(String uid,int start,int pageSize) {
		try {
			return orderDao.findOrderByUid(uid,start,pageSize);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	 //封装订单项数据
	 public ArrayList<Orderitem> findAllOrderItemByOid(String oid){
		 ArrayList<Orderitem>  orderitemList=new ArrayList<Orderitem>();
		  try {
			List<Map<String,Object>> mapList=orderDao.findAllOrderItemByOid(oid);
			 
			for(Map<String,Object> map : mapList){
				Orderitem orderitem=new Orderitem();
				BeanUtils.populate(orderitem, map);
				Product product=new Product();
				BeanUtils.populate(product, map);
				orderitem.setProduct(product);
				orderitemList.add(orderitem);
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  return orderitemList;
	 }
	 
	 public int findOrderCountByUid(String uid){
		 try {
			return orderDao.findOrderCountByUid(uid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return 0;
	 }
	 //查询所有的订单 并且分页
	 public List<Orders> findAllOrder(int start,int pageSize){
		  try {
			  List<Orders> orderList= orderDao.findAllOrder(start, pageSize);
			  for(Orders order :orderList){
				  String uid=order.getUid();
				  User user=userDao.findUserByuid(uid);
				  order.setUser(user);
			  }
			  return orderList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  return null;
		  
	 }
	 
	 //计算总页数
	 public int findOrderTotalPage(int pageSize){
		 try {
		 int totalCount=orderDao.findOrderCount();
		 int totalPage=totalCount%pageSize>0 ? totalCount/pageSize+1 :totalCount/pageSize;
		 return totalPage;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return 0;
	 }
}
