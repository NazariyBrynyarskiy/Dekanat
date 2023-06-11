<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Log in" %>
</h1>
<br/>

<form method="post" action="/account">
    <h3>Email<input type="text" name="email" required></h3>
    <h3>Password<input type="text" name="password" required></h3>
    <input type="submit" value="Submit">
</form>

</body>
</html>