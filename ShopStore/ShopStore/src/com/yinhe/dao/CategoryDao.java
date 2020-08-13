package com.yinhe.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.yinhe.bean.Category;
import com.yinhe.utils.DataSourceUtils;

public class CategoryDao {

	// 查找所有类型
	public List<Category> findAllCategory() throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from category";
		return runner.query(sql, new BeanListHandler<Category>(Category.class));
	}

	// 通过类型编号 查找类型对象
	public Category findCategoryByCid(String cid) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from category where cid=?";
		return runner.query(sql, new BeanHandler<Category>(Category.class), cid);
	}

	// 添加类型
	public void addCategory(String id, String cname) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "insert into category values(?,?)";
		runner.update(sql, id, cname);
	}

	// 删除类型
	public void deleteCategory(String id) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "delete from  category where cid=?";
		runner.update(sql, id);
	}

	// 更新类型
	public void updateCategory(String id, String cname) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "update category set cname=?   where cid=?";
		runner.update(sql, cname,id);
	}

}
