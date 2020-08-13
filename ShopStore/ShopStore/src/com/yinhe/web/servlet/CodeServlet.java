package com.yinhe.web.servlet;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String codes="abcdefghijklmnopqrstuvwxyzABCDEFJHIJKLMNOPQRSTUVWXYZ0123456789";	
		BufferedImage img=new BufferedImage(80,30,BufferedImage.TYPE_3BYTE_BGR);
		Graphics g=img.getGraphics();
		//填充图片
		g.setColor(new Color(255,255,255));
		g.fillRect(0, 0, 80, 30);
		//画入五个随机数
		Random rm=new Random();
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<5;i++){
			int index=rm.nextInt(codes.length());
			char code=codes.charAt(index);
			g.setColor(new Color(rm.nextInt(256),rm.nextInt(256),rm.nextInt(256)));
			g.setFont(new Font("宋体", Font.BOLD, 25));
			g.drawString(code+"", 2+15*i, 22);
			sb.append(code);
		}
		
		//画干扰线
		for(int i=0;i<10;i++){
			g.setColor(new Color(rm.nextInt(256),rm.nextInt(256),rm.nextInt(256)));
			g.drawLine(rm.nextInt(100), rm.nextInt(50), rm.nextInt(100), rm.nextInt(50));
		}
		
		//将验证码图片回写给浏览器
		response.setContentType("image/jpeg;charset=utf-8");
		OutputStream out=response.getOutputStream();
		ImageIO.write(img, "jpeg", out);
		
		//将随机字符串保存在session中
		request.getSession().setAttribute("code", sb.toString());
	}

}
