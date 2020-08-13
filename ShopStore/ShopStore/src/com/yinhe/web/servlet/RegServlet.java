package com.yinhe.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import com.yinhe.bean.User;
import com.yinhe.service.UserService;
import com.yinhe.utils.CommonsUtils;
import com.yinhe.utils.MailUtils;

public class RegServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService = new UserService();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		String code = request.getParameter("code");

		String sRand = (String) request.getSession().getAttribute("code");
		if (!code.equals(sRand)) {
			request.setAttribute("code_message", "验证码错误！");
			request.getRequestDispatcher("/register.jsp").forward(request, response);
			return;
		}
		Map<String, String[]> properties = request.getParameterMap();
		User user = new User();
		try {
			// 自定义类型转换器
			ConvertUtils.register(new Converter() {
				public Object convert(Class cla, Object value) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					try {
						return sdf.parse(value.toString());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				}
			}, Date.class);

			BeanUtils.populate(user, properties);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		user.setTelephone(null);
		String activeCode = CommonsUtils.getUUID();
		user.setUid(activeCode);
		user.setState(0);// 是否激活 0:表示未激活 1:表示激活
		user.setCode(activeCode);
		// 是否注册成功
		boolean isRegSuccess = userService.addUser(user);
		if (isRegSuccess) {
			// 发送激活邮件
			String emailMsg = "恭喜您注册成功，请点击下面的连接进行激活账户" + "<a href='http://localhost:8080/ShopStore/active?activeCode="
					+ activeCode + "'>" + "http://localhost:8080/ShopStore/active?activeCode=" + activeCode + "</a>";

			try {
				MailUtils.sendMail(user.getEmail(), emailMsg);
			} catch (AddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.sendRedirect(request.getContextPath() + "/registerSuccess.jsp");
		} else {
			response.sendRedirect(request.getContextPath() + "/registerFail.jsp");
		}
	}

}
