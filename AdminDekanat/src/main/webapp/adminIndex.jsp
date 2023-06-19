<%@ page import="java.io.IOException" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="com.nimbusds.jose.JOSEException" %>
<%@ page import="lecturerdb.dbenteties.GradeEntity" %>
<%@ page import="lecturerdb.dbenteties.StudentEntity" %>
<%@ page import="lecturermanagers.StudentManager" %>
<%@ page import="lecturermanagers.GradesManager" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="servlets.AccountController" %>
<%@ page import="studentservlets.CookiesController" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <title>Lecturer</title>
</head>
<body>
<br/>
<%
    try {
        AccountController accountController = new AccountController();
        accountController.doFilter(request, response, "admin");
    } catch (IOException | JOSEException | ParseException | ServletException e) {
        throw new RuntimeException(e);
    }
    CookiesController cookiesController = new CookiesController();
%>
<%= cookiesController.getToken(request) %>

<%
    StudentManager studentManager = null;
    GradesManager gradesManager = null;
    try {
        studentManager = new StudentManager();
        gradesManager = new GradesManager();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
%>
<hr>
<h1><%= "List of students:" %></h1>
<%
    for (StudentEntity studentEntity : studentManager.getStudentEntities()) {
%>
<p><h3><%= studentEntity %></h3>
<br>
<%
    }
%>
<br>
<hr>
<h1>List of grades</h1>
<%
    for (GradeEntity gradeEntity : gradesManager.getGradeEntities()) {
%>
<p><h3><%= gradeEntity %></h3>
<br>
<%
    }
%>


<br>
<hr>

<form method="post" action="/grades-controller">
    <h3>Subject name<input type="text" name="subjectName" required></h3>
    <h3>DekanatID<input type="number" name="dekanatID" required></h3>
    <h3>Grade<input type="number" name="grade" required></h3>
    <input type="submit" value="Submit">
</form>


<h1><a href="http://localhost:8080">Log out</a></h1>

</body>
</html>
