package com.yinhe.dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.yinhe.bean.Product;
import com.yinhe.utils.DataSourceUtils;

public class ProductDao {

	// 查找热门商品
	public List<Product> findHostProductList() throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where is_hot=? limit ?,?";
		return runner.query(sql, new BeanListHandler<Product>(Product.class), 1, 0, 9);
	}

	// 查找最新商品信息
	public List<Product> findNewProductList() throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product  order by pdate desc limit ?,?";
		return runner.query(sql, new BeanListHandler<Product>(Product.class), 0, 9);
	}

	// 通过分类 查询商品信息
	public List<Product> findProductByCategory(String cid, int start, int pageSize) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where cid=? limit ?,?";
		return runner.query(sql, new BeanListHandler<Product>(Product.class), cid, start, pageSize);
	}

	// 查询总条数
	public int getCount(String cid) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select count(*) from product where cid=?";
		Long count = (Long) runner.query(sql, new ScalarHandler(), cid);
		return count.intValue();

	}

	public Product findProductByPid(String pid) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where pid=?";
		return runner.query(sql, new BeanHandler<Product>(Product.class), pid);
	}

	// 查找产品信息
	public List<Product> findProduct(int start, int pageSize) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product  limit ?,?";
		return runner.query(sql, new BeanListHandler<Product>(Product.class), start, pageSize);
	}

	// 查询总条数
	public int getTotalCount() throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select count(*) from product ";
		Long count = (Long) runner.query(sql, new ScalarHandler());
		return count.intValue();
	}
	//添加产品
	public void addProduct(Product product) throws SQLException{
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql="insert into product values(?,?,?,?,?,?,?,?,?,?)";
		runner.update(sql, product.getPid(),product.getPname(),
				product.getMarket_price(),product.getShop_price(),
				product.getPimage(),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(product.getPdate()),product.getIs_hot(),
				product.getPdesc(),product.getPflag(),product.getCid());
	}
	
	//删除产品
	public void deleteProduct(String pid) throws SQLException{
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql="delete from product where pid=?";
		runner.update(sql, pid);
		
	}
	
	//修改产品
		public void updateProduct(Product product) throws SQLException{
			QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
			
			if(product.getPimage()==null){
				String sql ="update product set pname=?,market_price=?,shop_price=?,"
						+ "is_hot=?,pdesc=?,pflag=?,cid=? where pid=?";
				runner.update(sql, product.getPname(),
						product.getMarket_price(),
						product.getShop_price(),
						product.getIs_hot(),
						product.getPdesc(),
						product.getPflag(),
						product.getCid(),
						product.getPid()
						);
			}else{
				String sql ="update product set pname=?,market_price=?,shop_price=?,"
						+ "pimage=?,is_hot=?,pdesc=?,pflag=?,cid=? where pid=?";
				runner.update(sql, product.getPname(),
						product.getMarket_price(),
						product.getShop_price(),
						product.getPimage(),
						product.getIs_hot(),
						product.getPdesc(),
						product.getPflag(),
						product.getCid(),
						product.getPid()
						);
			}

			
		}
	
}
