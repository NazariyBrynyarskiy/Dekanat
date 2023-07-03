<%@ page import="java.io.IOException" %>
<%@ page import="servlets.AccountController" %>
<%@ page import="com.nimbusds.jose.JOSEException" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="studentservlets.StudentInfoController" %>
<%@ page import="studentservlets.GradesController" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="securityservlets.LocalizationController" %>
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
    LocalizationController localization = new LocalizationController();
    ResourceBundle messages = ResourceBundle.getBundle("messages", new Locale(localization.getLanguage(request)));

%>
<div class="container">
    <header>
    <span><a href="index">
        <%= studentInfoController.getInfo(request).get("Surname") %>
        <%= studentInfoController.getInfo(request).get("Name") %>
    </span>
        <span><a href="grades"> <%=messages.getString("gradesLink")%> </a></span>
        <span><a href="http://localhost:8080"> <%=messages.getString("logoutLink")%> </a></span>
        <form class="localizations" action="localization" method="post">
            <span><button type="submit" name="language" value="ua">&#x1F1FA;&#x1F1E6;</button></span>
            <span><button type="submit" name="language" value="en">&#x1F1FA;&#x1F1F8;</button></span>
        </form>
    </header>

    <main>
        <h1> <%=messages.getString("myGrades")%> </h1>
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
    <h4> <%=messages.getString("footer")%> </h4>
</footer>
</body>
</html>
