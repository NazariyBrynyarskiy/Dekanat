<%@ page import="java.io.IOException" %>
<%@ page import="servlets.AccountController" %>
<%@ page import="com.nimbusds.jose.JOSEException" %>
<%@ page import="java.text.ParseException" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <script src="${pageContext.request.contextPath}/js/clear-cookies.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <title>Student</title>
</head>
<body>
    <h1><%= "Hello User Dekanat!" %></h1>
    <br/>
    <div class="container">
        <header>
            <%
                try {
                    AccountController accountController = new AccountController();
                    accountController.doFilter(request, response, "user");
                } catch (IOException | JOSEException | ParseException | ServletException e) {
                    throw new RuntimeException(e);
                }
            %>
        </header>

        <main>
            <h1>My grades</h1>
        </main>
    </div>
    <br/>
    <h1><a href="http://localhost:8080" onclick="logout()">Log out</a></h1>
</body>
</html>