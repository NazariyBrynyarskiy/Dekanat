<%@ page import="manager.Redirecter" %>
<%@ page import="java.sql.SQLException" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Front end" %>
</h1>
<br/>

<form>
    <h3>Email<input type="text" name="email" required></h3>
    <h3>Password<input type="text" name="password" required></h3>

    <button type="submit">Reserve</button>
</form>

<%
    String email;
    String password;

    if (request.getParameter("email") != null &&
            request.getParameter("password") != null) {

        email = request.getParameter("email");
        password = request.getParameter("password");
        Redirecter redirecter = new Redirecter(email, password);

        try {
            response.sendRedirect(redirecter.url());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

%>

</body>
</html>