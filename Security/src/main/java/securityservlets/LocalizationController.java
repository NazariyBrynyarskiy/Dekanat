package securityservlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "securityservlets.LocalizationController", value = "/localization")

public class LocalizationController extends HttpServlet {

    public String getLanguage(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String language = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("language")) {
                    language = cookie.getValue();
                }
            }
        }
        return language;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getParameter("language") != null) {
            Cookie cookieLocalization = new Cookie("language", req.getParameter("language"));

            cookieLocalization.setPath("/");
            cookieLocalization.setMaxAge(2592000);
            resp.addCookie(cookieLocalization);

            resp.sendRedirect(req.getContextPath() + "/index");
        }
    }
}
