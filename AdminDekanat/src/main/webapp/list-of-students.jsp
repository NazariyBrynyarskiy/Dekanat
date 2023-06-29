<%@ page import="lecturerdb.dbenteties.StudentEntity" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="servlets.AccountController" %>
<%@ page import="java.io.IOException" %>
<%@ page import="com.nimbusds.jose.JOSEException" %>
<%@ page import="lecturerservlets.StudentManager" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="lecturerservlets.LecturerInfoController" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main-style.css">
    <title>Title</title>
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
        <%
            StudentManager studentManager = null;
            try {
                studentManager = new StudentManager();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        %>
        <hr>
        <h1><%= "List of students:" %></h1>
        <%
            for (StudentEntity studentEntity : studentManager.getStudentEntities(request)) {
        %>
        <p><h3><%= studentEntity %></h3>
        <br>
        <%
            }
        %>
    </main>
</div>

<footer>
    <h4>Пет проект</h4>
</footer>

</body>
</html>
