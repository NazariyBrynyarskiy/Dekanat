package lecturerservlets;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookiesController {

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
