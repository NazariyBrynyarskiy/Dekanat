<%@ page import="security.AccessToken" %>
<%@ page import="java.io.IOException" %>
<%@ page import="servlets.AccountController" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <script src="${pageContext.request.contextPath}/js/clear-cookies.js"></script>
    <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Hello User Dekanat!" %>
</h1>
<br/>
<h2>Cookies</h2>
<%
    try {
        AccountController accountController = new AccountController();
        accountController.doFilter(request, response, "user");
    } catch (IOException | ServletException e) {
        throw new RuntimeException(e);
    }

    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (!cookie.getName().equals("JSESSIONID")) {
%>
<h2><%= cookie.getName() %></h2>
<h3><%= cookie.getValue() %></h3>
<%
            }
        }
    }
%>
<br/>
<h1><a href="http://localhost:8080" onclick="logout()">Log out</a></h1>
</body>
</html>