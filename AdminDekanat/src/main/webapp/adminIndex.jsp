<%@ page import="db.studentinfo.StudentEntity" %>
<%@ page import="managers.student.StudentManager" %>
<%@ page import="db.studentgrades.GradeEntity" %>
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
    <h3>Subject<input type="text" name="subject" required></h3>
    <h3>Grade<input type="number" name="grade" required></h3>
    <h3>DekanatID<input type="number" name="dekanatID" required></h3>

    <button type="submit">Reserve</button>
</form>

<h1>--</h1>
<%= request.getParameter("grade") %>

<%
    final String URL = "http://localhost:8080/admin/index";
    String subject;
    int grade;
    int dekanatID;

    if (request.getParameter("subject") != null &&
            request.getParameter("grade") != null &&
            request.getParameter("dekanatID") != null) {
        subject = request.getParameter("subject");
        grade = Integer.parseInt(request.getParameter("grade"));
        dekanatID = Integer.parseInt(request.getParameter("dekanatID"));
        try {
            gradesManager.insert(subject, grade, dekanatID);
            response.sendRedirect(URL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

%>

</body>
</html>
