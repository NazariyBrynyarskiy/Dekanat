<%@ page import="db.dbenteties.StudentEntity" %>
<%@ page import="managers.student.StudentManager" %>
<%@ page import="db.dbenteties.GradeEntity" %>
<%@ page import="managers.grades.GradesManager" %>
<%@ page import="java.sql.SQLException" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
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
<h1><%= "List of students:" %></h1>
<%
    for (StudentEntity studentEntity : studentManager.getStudentEntities()) {
%>
<p><h3><%= studentEntity %></h3></p>
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
<p><h3><%= gradeEntity %></h3></p>
<br>
<%
    }
%>


<br>
<hr>

<form>
    <h3>Subject name<input type="text" name="subjectName" required></h3>
    <h3>DekanatID<input type="number" name="dekanatID" required></h3>
    <h3>Grade<input type="number" name="grade" required></h3>

    <button type="submit">Reserve</button>
</form>

<h1>--</h1>
<%= request.getParameter("grade") %>

<%
    final String URL = "http://localhost:8080/admin/index";
    String subjectName;
    int dekanatID;
    int grade;

    if (request.getParameter("subjectName") != null &&
            request.getParameter("dekanatID") != null &&
            request.getParameter("grade") != null) {
        subjectName = request.getParameter("subjectName");
        dekanatID = Integer.parseInt(request.getParameter("dekanatID"));
        grade = Integer.parseInt(request.getParameter("grade"));
        try {

            gradesManager.insert(subjectName, dekanatID, grade);
            response.sendRedirect(URL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

%>
<h1><a href="http://localhost:8080">Log out</a></h1>

</body>
</html>
