<%@ page import="java.io.IOException" %>
<%@ page import="servlets.AccountController" %>
<%@ page import="com.nimbusds.jose.JOSEException" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="studentservlets.StudentInfoController" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Student</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main-style.css">
</head>
<body>
<%
    try {
        AccountController accountController = new AccountController();
        accountController.doFilter(request, response, "user");
    } catch (IOException | JOSEException | ParseException e) {
        throw new RuntimeException(e);
    }
    StudentInfoController studentInfoController = new StudentInfoController();
%>
<div class="container">
    <header>
        <span><a href="index">
            <%= studentInfoController.getInfo(request).get("Surname") %>
            <%= studentInfoController.getInfo(request).get("Name") %></a>
        </span>
        <span><a href="grades">Оцінки</a></span>
        <span><a href="http://localhost:8080">Вийти</a></span>
    </header>

    <main>
        <h1>Загальна інформація</h1>
        <h3>Університет: <%= " LNU" %></h3>
        <h3>Факультет: <%= studentInfoController.getInfo(request).get("Faculty") %></h3>
        <h3>Спеціальність: <%= studentInfoController.getInfo(request).get("Speciality") %></h3>
        <h3>Група: <%= studentInfoController.getInfo(request).get("Group") %></h3>
        <h3>Форма навчання: <%= studentInfoController.getInfo(request).get("FormOfEducation") %></h3>
    </main>
</div>

<footer>
    <h4>Пет проект</h4>
</footer>
</body>
</html>