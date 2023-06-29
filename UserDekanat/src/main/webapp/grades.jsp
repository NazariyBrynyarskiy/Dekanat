<%@ page import="java.io.IOException" %>
<%@ page import="servlets.AccountController" %>
<%@ page import="com.nimbusds.jose.JOSEException" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="studentservlets.StudentInfoController" %>
<%@ page import="studentservlets.GradesController" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main-style.css">
    <title>Grades</title>
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
    GradesController gradesController = new GradesController();
%>
<div class="container">
    <header>
    <span><a href="index">
        <%= studentInfoController.getInfo(request).get("Surname") %>
        <%= studentInfoController.getInfo(request).get("Name") %>
    </span>
        <span><a href="grades">Оцінки</a></span>
        <span><a href="http://localhost:8080">Вийти</a></span>
    </header>

    <main>
        <h1>My grades</h1>
        <h3>
        <%
            try {
                for (Map.Entry<String, List<Integer>> entry : gradesController.getGrades(request).entrySet()) {
                    String subjectName = entry.getKey();
                    List<Integer> grades = entry.getValue();
        %>
                    <%= subjectName + ":" %>
        <%
                    for (Integer grade : grades) {
        %>
                        <%= grade %>
        <%
                    }
        %>
        <br>
        <%
                }
            } catch (ParseException | JOSEException e) {
                throw new RuntimeException(e);
            }
        %>
        </h3>

    </main>

</div>
<footer>
    <h4>Пет проект</h4>
</footer>
</body>
</html>
