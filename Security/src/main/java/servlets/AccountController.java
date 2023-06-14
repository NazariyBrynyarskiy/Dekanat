package servlets;

import com.nimbusds.jose.JOSEException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import security.AuthService;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;


@WebServlet(name = "servlets.AccountController", value = "/account")
public class AccountController extends HttpServlet {

    public AccountController() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private final String loginPage = "http://localhost:8080";


    public void doFilter(HttpServletRequest request, HttpServletResponse response, String role)
            throws IOException, ServletException, ParseException, JOSEException {

        String authToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    authToken = cookie.getValue();
                }
            }
        }

        if (authToken == null) {
            response.sendRedirect(loginPage);
            return;
        }

        AuthService token = new AuthService("", "");
        if (!token.authentication(response, authToken) || !token.authorization(response, authToken, role)) {
            response.sendRedirect(loginPage);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getParameter("email") != null &&
                request.getParameter("password") != null) {

            String email = request.getParameter("email");
            String password = request.getParameter("password");
            AuthService redirecter = new AuthService(email, password);


            try {
                redirecter.getUrl();
                if (redirecter.getRole() == null) {
                    RequestDispatcher dispatcher =
                            getServletContext().getRequestDispatcher("/error");
                    dispatcher.forward(request, response);
                }
            } catch (SQLException | ServletException e) {
                throw new RuntimeException(e);
            }

            Cookie accessToken = new Cookie("token", redirecter.getAccessToken().trim());
            Cookie role = new Cookie("role", redirecter.getRole().trim());
            accessToken.setPath("/");
            accessToken.setMaxAge(20);
            role.setPath("/");
            role.setMaxAge(20);
            response.addCookie(accessToken);
            response.addCookie(role);

            try {
                response.sendRedirect(redirecter.getUrl());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
