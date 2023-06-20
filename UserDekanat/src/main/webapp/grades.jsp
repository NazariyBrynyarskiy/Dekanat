<%@ page import="java.io.IOException" %>
<%@ page import="servlets.AccountController" %>
<%@ page import="com.nimbusds.jose.JOSEException" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="studentdb.dbentities.GradeEntity" %>
<%@ page import="studentservlets.GradesController" %>
<%@ page import="studentservlets.CookiesController" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <title>Grades</title>
</head>
<body>
<%
    try {
        AccountController accountController = new AccountController();
        accountController.doFilter(request, response);
    } catch (IOException | JOSEException | ParseException | ServletException e) {
        throw new RuntimeException(e);
    }
%>

<h1><%= "Hello User Dekanat!" %></h1>
<br/>
<div class="container">
    <header>
        <h1><a href="index">Home</a></h1>
    </header>

    <main>
        <h1>My grades</h1>
        <%
            GradesController gradesController = new GradesController();

            if (gradesController.getGrades(request) != null) {
                for (GradeEntity grade : gradesController.getGrades(request)) { %>
        <h3><%= grade.subjectName() + ", " + grade.grade()%></h3>
        <%      }
        }
            CookiesController cookiesController = new CookiesController();
        %>
        <h2><%= cookiesController.getToken(request) %></h2>
        <h2><%= cookiesController.getClientID(request) %></h2>
        <h2><%= cookiesController.getRole(request) %></h2>
    </main>
</div>
<br/>
</body>
</html>
