<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
 선생님 명단
</h1>
<table>
	<thead>
	<tr>
	<th>아이디</th> <th>이름</th> <th>주소</th> <th>H.P</th> <th>담당 학급</th> <th></th>
	</tr>
	</thead>
	<tbody>
	${teacherList }
	</tbody>
	
</table>
 <a href="/teamProject/insert">회원가입</a>
 <a href="/teamProject/login">로그인</a>
 <a href="/teamProject/findId">아이디 찾기</a> <br />

</body>
</html>
