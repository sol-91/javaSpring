package com.kopo.teamProject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.sql.SQLException;
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
		DB db = new DB("C:\\tomcat\\admin.sqlite", "admin");
		db.open();
		try {
			String result = db.selectData();
			model.addAttribute("teacherList", result);
		} catch (Exception e) {
			
		}
		db.close();
		return "home";
	}
	
//	占쎈립占쎈꼧
	@RequestMapping(value = "/insert", method = RequestMethod.GET)
	public String insert(Locale locale, Model model) {
		return "insert";
	}
	
//	3. insert.jsp占쎈퓠占쎄퐣 占쎈툡占쎌뒄占쎈립 占쎈쑓占쎌뵠占쎄숲�몴占� 獄쏆룇釉섓옙占쏙옙苑� 占쎌젟癰귣�占쏙옙 insert占쎈뻻�녹뮇夷�
	@RequestMapping(value = "/insert_action", method = RequestMethod.POST)
	public String insertAction(Locale locale, Model model, HttpServletRequest request) throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8");
		String name = request.getParameter("name");
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		String nclass = request.getParameter("nclass");
		String address = request.getParameter("address");
		String phone = request.getParameter("phone");
//		
		DB db = new DB("C:\\tomcat\\admin.sqlite", "admin");
		db.open();
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(pw.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				 hexString.append(hex);
				 pw = hexString.toString();
//				 teacher.pw = hexString.toString();
			}
			System.out.println(hexString.toString());
			Teacher teacher = new Teacher(name, id, pw, nclass, address, phone);
			db.insertData(teacher); //패스워드를 sha-256 기반으로 hexString 함. 

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		db.close();
		return "redirect:/";
	}
	
//	id 찾기
	@RequestMapping(value = "/findId", method = RequestMethod.GET)
	public String findId(Locale locale, Model model) {
		return "findId";
	}
	
	@RequestMapping(value = "/findId_action", method = RequestMethod.POST)
	public String findIdAction(Locale locale, Model model, HttpServletRequest request) throws UnsupportedEncodingException, SQLException {
		boolean findId = false;
		request.setCharacterEncoding("UTF-8");
		String name = request.getParameter("name");
		String nclass = request.getParameter("nclass");
		String phone = request.getParameter("phone");
		Teacher check = new Teacher(name, nclass, phone);
		System.out.println(check.name);
		DB db = new DB("C:\\tomcat\\admin.sqlite", "admin");
		db.open();
		
		findId = db.checkIdData(check);
		if (findId) {
			Teacher teacher = db.selectData(check);
			model.addAttribute("name", teacher.name);
			model.addAttribute("id", teacher.id);
			db.close();
			return "success_findId";
		}
		else {
			db.close();
			return "findId";
		}
	}

	
	   @RequestMapping(value = "/login_action", method = RequestMethod.POST)
	//   public String login_action(Locale locale, Model model
//	         , HttpServletRequest request, @RequestParam("id") String id, @RequestParam("password") String password) {
	   public String login_action(Locale locale, Model model, HttpServletRequest request) throws UnsupportedEncodingException {
	      request.setCharacterEncoding("UTF-8");
	      String id = request.getParameter("id");
	      String pw = request.getParameter("pw");
	      DB db = new DB("C:\\tomcat\\admin.sqlite", "admin");
	      db.open();
	      
	      boolean result = false;
//	      password를 sha256을 이용해서 해시처리함 (보안)
	      try {
	         MessageDigest digest = MessageDigest.getInstance("SHA-256");
	         byte[] hash = digest.digest(pw.getBytes("UTF-8"));
	         StringBuffer hexString = new StringBuffer();

	         for (int i = 0; i < hash.length; i++) {
	            String hex = Integer.toHexString(0xff & hash[i]);
	            if (hex.length() == 1)
	               hexString.append('0');
	            hexString.append(hex);
	         }
	         result = db.login(id, hexString.toString());
	         
////	         로그인 성공 시 로그인된 계정의 id와 name을 표시
//	         String htmlText = db.selectData(id, hexString.toString());
//	         model.addAttribute("loginInfo", htmlText);

	      } catch (Exception ex) {
	         throw new RuntimeException(ex);
	      }
	      db.close();
	      
//	      로그인 성공 시 result = true & is_login = true
	      if (result) {
	         HttpSession session = request.getSession();
	         session.setAttribute("is_login", "true");
	         model.addAttribute("id", id);
	         return "success";
	      } else {
	         return "fail";
	      }
	   }
	
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(Locale locale, Model model, HttpServletRequest request ) {
			HttpSession session = request.getSession();
			session.invalidate();
			return "login";
	}
	
	@RequestMapping(value = "/private", method = RequestMethod.GET)
	public String privatePage(Locale locale, Model model, HttpServletRequest request ) {
			HttpSession session = request.getSession();
			String isLogin = (String) session.getAttribute("is_login");
			if (isLogin != null && isLogin.equals("true")) {
				return "private";
			}
			return "login";
	}
	
	//PJH
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String update(Locale locale, Model model, HttpServletRequest request) {
		String id = (String) request.getParameter("id");
		model.addAttribute("id", id);
		String dbUrl = "C:\\tomcat\\admin.sqlite";
		System.out.println(id);
		System.out.println(model.addAttribute("id", id));
		DB db = new DB(dbUrl, "admin");
		db.open();
		try {
			Teacher teacher = db.selectData(id);
			model.addAttribute("name", teacher.name);
			model.addAttribute("id", teacher.id);
			model.addAttribute("pw", teacher.pw);
			model.addAttribute("nclass", teacher.nclass);
			model.addAttribute("address", teacher.address);
			model.addAttribute("phone", teacher.phone);
		} catch (Exception e) {
			// TODO: handle exception
		}
		db.close();
		
		return "update";
	}	
	
	@RequestMapping(value = "/update_action", method = RequestMethod.POST)
	public String updateAction(Locale locale, Model model, HttpServletRequest request) throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8");
		
		String name = (String) request.getParameter("name");
		String id = (String) request.getParameter("id");
		String pw = (String) request.getParameter("pw");
		String nclass = (String) request.getParameter("nclass");
		String address = (String) request.getParameter("address");
		String phone = (String) request.getParameter("phone");
		Teacher teacher = new Teacher(name, id, pw, nclass, address, phone);
		
		model.addAttribute("id",id);
		String dbUrl = "C:\\tomcat\\admin.sqlite";
		DB db = new DB(dbUrl, "admin");
		db.open();
		
		try {
			db.updateData(teacher);
			
		} catch (Exception e) {
			
		}
		db.close();
		return "redirect:/";
	}
	
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String tabledelete(Locale locale, Model model, HttpServletRequest request) {
		String id = (String) request.getParameter("id");
		System.out.println(id);
		model.addAttribute("id", id);
		System.out.println(model.addAttribute("id", id));
		DB db = new DB("C:\\tomcat\\admin.sqlite", "admin");
		db.open();
		try {
			Teacher teacher = db.selectData(id);
			model.addAttribute("name", teacher.name);
			model.addAttribute("nclass", teacher.nclass);
			model.addAttribute("address", teacher.address);
			model.addAttribute("phone", teacher.phone);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		db.close();
		return "delete";
	}
	

	@RequestMapping(value = "/delete_action", method = RequestMethod.POST)
	public String deleteAction(Locale locale, Model model, HttpServletRequest request) throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8");
		String id = (String) request.getParameter("id");
		model.addAttribute("id", id);
		DB db = new DB("C:\\tomcat\\admin.sqlite", "admin") ;
		db.open();
		try {
			Teacher teacher = new Teacher(id);
			db.deleteData(teacher);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		db.close();
		return "success";
	}
	
}
