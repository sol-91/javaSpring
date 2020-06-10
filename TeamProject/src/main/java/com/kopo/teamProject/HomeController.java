package com.kopo.teamProject;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	
//	김재영
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String tabledelete(Locale locale, Model model, HttpServletRequest request) {
		String id = (String) request.getParameter("id");
		//idx의 경우 int로 받을수도 있지만 기본적으로 get으로 받는 모든 데이터는 스트링으로 받아서 나중에 형변환한다.
		model.addAttribute("id", id);
		DB dataReader = new DB("C:\\tomcat\\admin.sqlite", "admin");
		dataReader.open();
		try {
			Theacher teacher = dataReader.searchData(id);
			model.addAttribute("name", teacher.name);
			model.addAttribute("kor", teacher.kor);
			model.addAttribute("eng", teacher.eng);
			model.addAttribute("math", teacher.math);

		} catch (Exception e) {
			// TODO: handle exception
		}
		dataReader.close();
		return "delete";
	}
	

	@RequestMapping(value = "/deletestudent_action", method = RequestMethod.GET)
	//// UnsupportedEncodingException을 쓰는 이유는  encoder를 쓸때 다른 유형이 올 수도 있기 때문에 항상 있어야함
	public String tabledeleteAction(Locale locale, Model model,  @RequestParam("id") String id) {
		model.addAttribute("id", id);
		DB dataReader = new DB("C:\\tomcat\\admin.sqlite", "admin") ;
		dataReader.open();
		try {
			
			dataReader.deleteData(id);
			String result = dataReader.searchData();
			model.addAttribute("html",result);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		dataReader.close();
		return "delete";
	}
	
//	한솔
	@RequestMapping(value = "/insert", method = RequestMethod.GET)
	public String insert(Locale locale, Model model) {
		return "insert";
	}
	
//	3. insert.jsp에서 필요한 데이터를 받아와서 정보를 insert시켜줌
	@RequestMapping(value = "/insert_action", method = RequestMethod.POST)
	public String insertAction(Locale locale, Model model, HttpServletRequest request) throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8");
		String name = request.getParameter("name");
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		String nclass = request.getParameter("nclass");
		String address = request.getParameter("address");
		int phone = Integer.parseInt(request.getParameter("phone"));
		Teacher teacher = new Teacher(name, id, pw, nclass, address, phone);
		DB dataReader = new DB("C:\\tomcat\\admin.sqlite", "admin");
		dataReader.open();
		try {
			dataReader.insertData(teacher);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dataReader.close();
		return "redirect:/";
	}
}
