package com.yinhe.service;

import java.sql.SQLException;
import java.util.List;

import com.yinhe.bean.Category;
import com.yinhe.dao.CategoryDao;

public class CategoryService {

	private CategoryDao categoryDao=new CategoryDao();
	
	//查找所有类型
	public List<Category> findAllCategory(){
		try {
			return categoryDao.findAllCategory();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	//通过类型编号 查找类型对象
	public Category findCategoryByCid(String cid){
		try {
			return categoryDao.findCategoryByCid(cid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	//添加类型
	public void addCategory(String id,String cname){
		try {
			categoryDao.addCategory(id, cname);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//删除
	public void deleteCategory(String id) {
		try {
			categoryDao.deleteCategory(id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//修改
	public void updateCategory(String id, String cname) {
		try {
			categoryDao.updateCategory(id, cname);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
