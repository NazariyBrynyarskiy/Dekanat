<%@ page import="lecturerdb.dbenteties.StudentEntity" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="servlets.AccountController" %>
<%@ page import="java.io.IOException" %>
<%@ page import="com.nimbusds.jose.JOSEException" %>
<%@ page import="lecturerservlets.StudentsController" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="lecturerservlets.LecturerInfoController" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="securityservlets.LocalizationController" %>
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
    LocalizationController localization = new LocalizationController();
    ResourceBundle messages = ResourceBundle.getBundle("messages", new Locale(localization.getLanguage(request)));
%>
<div class="container">
    <header>
        <span><a href="index">
            <%= lecturerInfoController.getInfo(request).get("Surname") %>
            <%= lecturerInfoController.getInfo(request).get("Name") %></a>
        </span>
        <span><a href="grades"> <%=messages.getString("gradesLink")%> </a></span>
        <span><a href="students"> <%=messages.getString("listOfStudentsLink")%> </a></span>
        <span><a href="http://localhost:8080"> <%=messages.getString("logoutLink")%> </a></span>
        <form class="localizations" action="localization" method="post">
            <span><button type="submit" name="language" value="ua">&#x1F1FA;&#x1F1E6;</button></span>
            <span><button type="submit" name="language" value="en">&#x1F1FA;&#x1F1F8;</button></span>
        </form>
    </header>

    <main>
        <%
            StudentsController studentManager = null;
            try {
                studentManager = new StudentsController();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        %>
        <hr>
        <h1><%=messages.getString("listOfStudents")%></h1>
        <%
            for (Map.Entry<String, List<StudentEntity>> entry : studentManager.getStudentEntities(request).entrySet()) {
                String groupName = entry.getKey();
                List<StudentEntity> students = entry.getValue();
        %>
        <h2><%= groupName + ":" %></h2>
        <%
                for (StudentEntity student : students) {
        %>
        <h3><%= student %></h3>
        <%
                }
            }
        %>
    </main>
</div>

<footer>
    <h4> <%=messages.getString("footer")%> </h4>
</footer>

</body>
</html>
