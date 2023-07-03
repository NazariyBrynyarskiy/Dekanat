<%@ page import="lecturerdb.dbenteties.GradeEntity" %>
<%@ page import="servlets.AccountController" %>
<%@ page import="java.io.IOException" %>
<%@ page import="com.nimbusds.jose.JOSEException" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="lecturerservlets.LecturerInfoController" %>
<%@ page import="lecturerservlets.GradesController" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
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
    GradesController gradesController = new GradesController();
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
        <form method="post" action="/grades-controller">
            <h3> <%=messages.getString("subject")%> <input type="text" name="subjectName" required></h3>
            <h3> <%=messages.getString("dekanatID")%> <input type="number" name="dekanatID" required></h3>
            <h3> <%=messages.getString("grade")%> <input type="number" name="grade" required></h3>
            <input type="submit" value=<%=messages.getString("add")%>>
        </form>
        <h1> <%=messages.getString("listOfGrades")%> </h1>
        <%
            for (Map.Entry<String, List<GradeEntity>> entry : gradesController.getGradeEntities(request).entrySet()) {
                String subjectName = entry.getKey();
                List<GradeEntity> grades = entry.getValue();
        %>
        <h2><%= subjectName + ":" %></h2>
        <%
            for (GradeEntity grade : grades) {
        %>
        <h3><%= grade %></h3>
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
