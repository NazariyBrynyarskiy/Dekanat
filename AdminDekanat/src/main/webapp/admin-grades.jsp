<%@ page import="lecturerdb.dbenteties.GradeEntity" %>
<%@ page import="servlets.AccountController" %>
<%@ page import="java.io.IOException" %>
<%@ page import="com.nimbusds.jose.JOSEException" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="lecturerservlets.LecturerInfoController" %>
<%@ page import="lecturerservlets.GradesController" %>
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
    GradesController gradesController = new GradesController();
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
        <form method="post" action="/grades-controller">
            <h3>Subject name<input type="text" name="subjectName" required></h3>
            <h3>DekanatID<input type="number" name="dekanatID" required></h3>
            <h3>Grade<input type="number" name="grade" required></h3>
            <input type="submit" value="Submit">
        </form>
        <h1>List of grades</h1>
        <%
            for (GradeEntity gradeEntity : gradesController.getGradeEntities(request)) {
        %>
        <p><h3><%= gradeEntity %></h3>
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
