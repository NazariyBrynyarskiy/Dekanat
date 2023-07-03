<%@ page import="java.util.ResourceBundle" %>
<%@ page import="java.util.Locale" %>
<%@ page import="securityservlets.LocalizationController" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main-style.css">
    <title>Login</title>
</head>
<body>
<div class="container">
    <form class="localizations" action="localization" method="post">
        <span><button type="submit" name="language" value="ua">&#x1F1FA;&#x1F1E6;</button></span>
        <span><button type="submit" name="language" value="en">&#x1F1FA;&#x1F1F8;</button></span>
    </form>
    <%
        LocalizationController localization = new LocalizationController();
        ResourceBundle messages = ResourceBundle.getBundle("messages", new Locale(localization.getLanguage(request)));
    %>
    <h1> <%=messages.getString("loginLink")%> </h1>

    <br/>

    <form method="post" action="/account">
        <h3> <%=messages.getString("email")%> <input type="text" name="email" required></h3>
        <h3> <%=messages.getString("password")%> <input type="text" name="password" required></h3>
        <input type="submit" value=<%=messages.getString("loginLink")%>>
    </form>
</div>

</body>
</html>