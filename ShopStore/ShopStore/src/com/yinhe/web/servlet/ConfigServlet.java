package com.yinhe.web.servlet;


import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
public class ConfigServlet extends HttpServlet {

	static  HashMap<String,Object>  map=new  HashMap<String,Object>();

	
	public static HashMap<String, Object> getMap() {
		return map;
	}


	public static void setMap(HashMap<String, Object> map) {
		ConfigServlet.map = map;
	}


	public void init(ServletConfig config) throws ServletException {
	
		
		//dom4j½âÎöxmlÎÄ¼þ
		try {
			SAXReader reader=new SAXReader();
			Document document=reader.read(ConfigServlet.class.getClassLoader().getResource("bean.xml").getPath());

			Element rootElement=document.getRootElement();
			Iterator childElement= rootElement.elementIterator();
			while(childElement.hasNext()){
			   Element beanElement=	(Element) childElement.next();
			   String key=beanElement.attributeValue("id");
			   String value=beanElement.attributeValue("class");
			   map.put(key, Class.forName(value).newInstance());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
}
