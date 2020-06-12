<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/login.css">
<title>Insert title here</title>
</head>
<body>
<p> 정상적으로 로그인 되었습니다. </p>

<a href="/teamProject/">홈 페 이 지</a><br />
<a href="/teamProject/update?id=${id }">개인정보수정</a><br />
<a href="/teamProject/delete?id=${id }">선생정보삭제</a><br />
<a href="/teamProject/logout">로그아웃</a>
</body>
</html>