package com.yinhe.web.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.yinhe.bean.Cart;
import com.yinhe.bean.CartItem;
import com.yinhe.bean.Category;
import com.yinhe.bean.Product;
import com.yinhe.bean.User;
import com.yinhe.service.CategoryService;
import com.yinhe.service.ProductService;

public class ProductServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private int pageSize = 10;
	private int currentPage = 1;
	private ProductService productService = new ProductService();
	private CategoryService categoryService = new CategoryService();

	// protected void doGet(HttpServletRequest request, HttpServletResponse
	// response) throws ServletException, IOException {
	//
	// this.doPost(request, response);
	// }
	//
	//
	// protected void doPost(HttpServletRequest request, HttpServletResponse
	// response) throws ServletException, IOException {
	//
	// String method=request.getParameter("method");
	// if("index".equals(method)){
	// this.index(request, response);
	// }else if("findProductByCategory".equals(method)){
	// this.findProductByCategory(request, response);
	// }else if("findProductInfoByPid".equals(method)){
	// this.findProductInfoByPid(request, response);
	// }
	//
	// }

	public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Product> hostProductList = productService.findHostProductList();
		// 最新商品
		List<Product> newProductList = productService.findNewProductList();
		// List<Category> categoryList = categoryService.findAllCategory();
		request.setAttribute("hostProductList", hostProductList);
		request.setAttribute("newProductList", newProductList);
		// request.setAttribute("categoryList", categoryList);
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}

	public void findProductByCategory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String cid = request.getParameter("cid");
		if (request.getParameter("currentPage") != null) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		int start = (currentPage - 1) * pageSize;
		List<Product> productList = productService.findProductByCategory(cid, start, pageSize);

		int totalCount = productService.getCount(cid);

		int totalPage = totalCount % pageSize > 0 ? totalCount / pageSize + 1 : totalCount / pageSize;
		request.setAttribute("productList", productList);
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("cid", cid);

		// 浏览记录
		List<Product> historyList = new ArrayList<Product>();
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("pids".equals(cookie.getName())) {
					String pids = cookie.getValue();
					String[] splits = pids.split("-");
					for (int i = 0; i < splits.length; i++) {
						String pid = splits[i];
						Product product = productService.findProductByPid(pid);
						historyList.add(product);
					}
				}
			}
		}

		request.setAttribute("historyList", historyList);
		request.getRequestDispatcher("product_list.jsp").forward(request, response);
	}

	public void findProductInfoByPid(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pid = request.getParameter("pid");
		Product product = productService.findProductByPid(pid);
		Category category = categoryService.findCategoryByCid(product.getCid() + "");
		product.setCategory(category);
		request.setAttribute("product", product);

		String pids = pid;
		// 将pid放入cookie中
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("pids".equals(cookie.getName())) {
					// 将值 3-2-8存放
					pids = cookie.getValue();
					String[] split = pids.split("-");
					List<String> aslist = Arrays.asList(split);
					LinkedList<String> list = new LinkedList<String>(aslist);
					if (list.contains(pid)) {
						list.remove(pid);
						list.addFirst(pid);
					} else {
						list.addFirst(pid);
					}
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < list.size(); i++) {
						sb.append(list.get(i)).append("-");
					}
					sb.deleteCharAt(sb.length() - 1);
					pids = sb.toString();
				}

			}
		}

		Cookie cookie = new Cookie("pids", pids);
		response.addCookie(cookie);

		request.getRequestDispatcher("product_info.jsp").forward(request, response);
	}

	public void addProductToCard(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		   //获取商品编号
		   String pid= request.getParameter("pid");
		   //购买数量
		   int buyNum=Integer.parseInt(request.getParameter("buyNum"));
		   Product product= productService.findProductByPid(pid);
		   HttpSession session=request.getSession();
		   Cart cart= (Cart) session.getAttribute("cart");
		   if(cart==null){
			   cart=new Cart();
		   }
		   CartItem cartItem=new CartItem();
		   cartItem.setBuyNum(buyNum);
		   cartItem.setProduct(product);
		   //计算小计
		   double subTotal=product.getShop_price()*buyNum;
		   cartItem.setSubTotal(subTotal);
		   
		   HashMap<String,CartItem> cartItems= cart.getCartItems();
		   double newSubTotal=0;
		   if(cartItems.containsKey(pid)){
			   CartItem item= cartItems.get(pid);
			   //修改数量
			   int oldBuyNum= item.getBuyNum();
			   oldBuyNum=oldBuyNum+buyNum;
			   item.setBuyNum(oldBuyNum);
			   //修改小计
			   double oldSubTotal= item.getSubTotal();
			   newSubTotal=buyNum*product.getShop_price();
			   oldSubTotal=oldSubTotal+newSubTotal;
			   item.setSubTotal(oldSubTotal);
		   }else{
			   cartItems.put(pid, cartItem); 
			   newSubTotal=buyNum*product.getShop_price();
		   }
		   
		   //计算 总计
		   double total=cart.getTotal()+newSubTotal;
		   cart.setTotal(total);
		   cart.setCartItems(cartItems);
		   session.setAttribute("cart", cart);
		   request.getRequestDispatcher("cart.jsp").forward(request, response);
	}
	
	//删除指定的商品
	public void deleteCartByPid(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		  //获取商品编号
		   String pid= request.getParameter("pid");
		   HttpSession session= request.getSession();
		   Cart cart= (Cart) session.getAttribute("cart");
		   if(cart !=null){
			  HashMap<String,CartItem> cartItems= cart.getCartItems();
			  //修改总价
			  CartItem cartItem=cartItems.get(pid);
			  cart.setTotal(cart.getTotal()-cartItem.getSubTotal());
			  cartItems.remove(pid);
			  cart.setCartItems(cartItems);
		   }
		   
		   session.setAttribute("cart",cart);
		   request.getRequestDispatcher("cart.jsp").forward(request, response);
	}
	//清空购物车
	public void clearCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		HttpSession session= request.getSession();
		if(session.getAttribute("cart")!=null){
			session.removeAttribute("cart");
		}
		
		request.getRequestDispatcher("cart.jsp").forward(request, response);
	}
	
	public void findProduct(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		if (request.getParameter("currentPage") != null) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		int start = (currentPage - 1) * pageSize;
		List<Product> productList = productService.findProduct(start, pageSize);

		int totalCount = productService.getTotalCount();

		int totalPage = totalCount % pageSize > 0 ? totalCount / pageSize + 1 : totalCount / pageSize;
		request.setAttribute("productList", productList);
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("currentPage", currentPage);
		request.getRequestDispatcher("/admin/product/list.jsp").forward(request, response);
	}
	//添加产品
	public void addProduct(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, FileUploadException{
		
		 DiskFileItemFactory fileItemFactory=new DiskFileItemFactory();
          ServletFileUpload servletFileUpload=new ServletFileUpload(fileItemFactory);
          HashMap<String,Object> map=new HashMap<String,Object>();
          if(servletFileUpload.isMultipartContent(request)){
        	  List<FileItem> itemList= servletFileUpload.parseRequest(request);
        	  for(FileItem item :itemList){
        		   boolean formItem= item.isFormField();
        		   if(formItem){
        			   
        			  String fieldName= item.getFieldName();
        			  String value= item.getString("UTF-8");
        			  map.put(fieldName, value);
        		   }else{
        			   //文件上传
        			   InputStream is= item.getInputStream();
        			   String fileName=item.getName();
        			   String realPath= request.getRealPath("products");
        			   File file=new File(realPath,fileName);
        			   FileOutputStream fos=new FileOutputStream(file);
        			   IOUtils.copy(is, fos);
        			   
        			   fos.close();
        			   is.close();
        			   item.delete();
        			   map.put("pimage", "products/"+fileName);
        		   }
        	  }
        	  Product product=new Product();
        	  try {
				BeanUtils.populate(product, map);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	  productService.addProduct(product);
        	  request.getRequestDispatcher("product?method=findProduct").forward(request, response);
          }         
	}
	
	public void deleteProduct(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, FileUploadException{
		String pid=request.getParameter("pid");
		Product product= productService.findProductByPid(pid);
	    String pimage=	product.getPimage();
		productService.deleteProduct(pid);
		
		//删除服务器的图片
	
		String realPath=request.getRealPath("/");
		String path=realPath+"/"+pimage;
		File file=new File(path);
		if(file.exists()){
			file.delete();
		}
		request.getRequestDispatcher("product?method=findProduct").forward(request, response);
	}

	public void toUpdateProduct(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, FileUploadException{
		String pid=request.getParameter("pid");
		Product product= productService.findProductByPid(pid);
		List<Category> categoryList=categoryService.findAllCategory();
		request.setAttribute("product", product);
		request.setAttribute("categoryList", categoryList);
		request.getRequestDispatcher("/admin/product/edit.jsp").forward(request, response);
	}
	
	
	//修改产品
		public void updateProduct(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException, FileUploadException{
			
			  DiskFileItemFactory fileItemFactory=new DiskFileItemFactory();
	          ServletFileUpload servletFileUpload=new ServletFileUpload(fileItemFactory);
	          HashMap<String,Object> map=new HashMap<String,Object>();
	          if(servletFileUpload.isMultipartContent(request)){
	        	  List<FileItem> itemList= servletFileUpload.parseRequest(request);
	        	  for(FileItem item :itemList){
	        		   boolean formItem= item.isFormField();
	        		   if(formItem){
	        			   
	        			  String fieldName= item.getFieldName();
	        			  String value= item.getString("UTF-8");
	        			  map.put(fieldName, value);
	        		   }else{
	        			   
	        			   if(item.getSize()>0){
	        				   //文件上传
		        			   InputStream is= item.getInputStream();
		        			   String fileName=item.getName();
		        			   String realPath= request.getRealPath("products");
		        			   File file=new File(realPath,fileName);
		        			   FileOutputStream fos=new FileOutputStream(file);
		        			   IOUtils.copy(is, fos);
		        			   
		        			   fos.close();
		        			   is.close();
		        			   item.delete();
		        			   map.put("pimage", "products/"+fileName);
	        			   }
	        			 
	        		   }
	        	  }
	        	  Product product=new Product();
	        	  try {
					BeanUtils.populate(product, map);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	  productService.updateProduct(product);
	        	  request.getRequestDispatcher("product?method=findProduct").forward(request, response);
	          }         
		}
}
