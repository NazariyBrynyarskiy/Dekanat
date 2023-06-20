<%@ page import="java.io.IOException" %>
<%@ page import="servlets.AccountController" %>
<%@ page import="com.nimbusds.jose.JOSEException" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="studentdb.dbaccess.FacultyDBAccess" %>
<%@ page import="studentdb.dbaccess.interfaces.SelectFromDB" %>
<%@ page import="studentservlets.StudentInfoController" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <script src="${pageContext.request.contextPath}/js/clear-cookies.js"></script>
    <title>Student</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main-style.css">
</head>
<body>
<%
    try {
        AccountController accountController = new AccountController();
        accountController.doFilter(request, response);
        accountController.doFilter(request, response);
    } catch (IOException | JOSEException | ParseException | ServletException e) {
        throw new RuntimeException(e);
    }
    StudentInfoController studentInfoController = new StudentInfoController();
%>
<div class="container">
    <header>
        <span><a href="grades">Оцінки</a></span>
        <span><a href="http://localhost:8080" onclick="logout()">Вийти</a></span>
    </header>

    <main>
        <h1>Загальна інформація</h1>
        <h3>Університет: <%= " ЛНУ" %></h3>
        <h3>Факультет: <%= studentInfoController.getFaculty(request) %></h3>
        <h3>Спеціальність</h3>
        <h3>Група</h3>
        <h3>Форма навчання</h3>
    </main>
</div>

<footer>
    <h4>Пет проект</h4>
</footer>
</body>
</html>