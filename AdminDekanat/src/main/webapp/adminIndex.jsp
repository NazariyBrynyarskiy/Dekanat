<%@ page import="java.io.IOException" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="com.nimbusds.jose.JOSEException" %>
<%@ page import="servlets.AccountController" %>
<%@ page import="lecturerservlets.LecturerInfoController" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main-style.css">
    <title>Lecturer</title>
</head>
<body>
<%
    try {
        AccountController accountController = new AccountController();
        accountController.doFilter(request, response, "admin");
    } catch (IOException | JOSEException | ParseException e) {
        throw new RuntimeException(e);
    }
    LecturerInfoController lecturerInfoController = new LecturerInfoController();
%>
<div class="container">
    <header>
        <span><a href="index">
            <%= lecturerInfoController.getInfo(request).get("Surname") %>
            <%= lecturerInfoController.getInfo(request).get("Name") %></a>
        </span>
        <span><a href="grades">Оцінки</a></span>
        <span><a href="students">Список_студентів</a></span>
        <span><a href="http://localhost:8080">Вийти</a></span>
    </header>

    <main>
        <h1>Загальна інформація</h1>
        <h3>Університет: <%= " LNU" %></h3>
        <h3>Факультет: <%= lecturerInfoController.getInfo(request).get("Faculty") %></h3>
    </main>
</div>

<footer>
    <h4>Пет проект</h4>
</footer>
</body>
</html>
