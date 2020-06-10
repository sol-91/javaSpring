package com.kopo.teamProject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
	
	
//	源��옱�쁺
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String tabledelete(Locale locale, Model model, HttpServletRequest request) {
		String id = (String) request.getParameter("id");
		//idx�쓽 寃쎌슦 int濡� 諛쏆쓣�닔�룄 �엳吏�留� 湲곕낯�쟻�쑝濡� get�쑝濡� 諛쏅뒗 紐⑤뱺 �뜲�씠�꽣�뒗 �뒪�듃留곸쑝濡� 諛쏆븘�꽌 �굹以묒뿉 �삎蹂��솚�븳�떎.
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
	//// UnsupportedEncodingException�쓣 �벐�뒗 �씠�쑀�뒗  encoder瑜� �벝�븣 �떎瑜� �쑀�삎�씠 �삱 �닔�룄 �엳湲� �븣臾몄뿉 �빆�긽 �엳�뼱�빞�븿
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
	
//	�븳�넄
	@RequestMapping(value = "/insert", method = RequestMethod.GET)
	public String insert(Locale locale, Model model) {
		return "insert";
	}
	
//	3. insert.jsp�뿉�꽌 �븘�슂�븳 �뜲�씠�꽣瑜� 諛쏆븘���꽌 �젙蹂대�� insert�떆耳쒖쨲
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
		DB db = new DB("C:\\tomcat\\admin.sqlite", "admin");
		db.open();
		try {
			db.insertData(teacher);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		db.close();
		return "redirect:/";
	}
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Locale locale, Model model) {
	
		return "login";
	}
	@RequestMapping(value = "/login_action", method = RequestMethod.POST)
	public String login(Locale locale, Model model, HttpServletRequest request) throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
		//유저에게 id와 password를 파라미터로 전달 받음
		String id = request.getParameter("id");
		String password = request.getParameter("password");
		DB db = new DB("C:\\tomcat\\admin.sqlite", "admin");
		db.open();
		//로그인 정보와 DB 정보의 일치여부를 확인히가 위한 boolean 변수 
		boolean result = false;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(password.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			System.out.println(hexString.toString());
			//로그인 정보와 DB정보가 일치하는 경우 result는 true로 변환
			result = db.login(id, hexString.toString());

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		db.close();
		//result가 true일 경우 session 유지를 진행
		if (result) {
			//session setting
			HttpSession session = request.getSession();
			session.setAttribute("is_login", "true");
			return "secondhome";
		}else {
			//result가 false일 경우 session 만료
			return "fail";
		}

	}
}
