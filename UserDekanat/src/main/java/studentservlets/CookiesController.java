package studentservlets;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;

public class CookiesController extends HttpServlet {

    public String getToken(HttpServletRequest request) {
        String authToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    authToken = cookie.getValue();
                }
            }
        }
        return authToken;
    }
}
