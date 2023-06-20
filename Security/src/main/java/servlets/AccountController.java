package servlets;

import com.nimbusds.jose.JOSEException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import security.AuthService;
import security.token.Token;

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


    public void doFilter(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, ParseException, JOSEException {

        int cookieClientID = 0;
        String cookieRole = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("clientID")) {
                    cookieClientID = Integer.parseInt(cookie.getValue());
                }
                if (cookie.getName().equals("role")) {
                    cookieRole = cookie.getValue();
                }
            }
        }

        AuthService.authorization(request, response, cookieClientID, cookieRole);
        if (!AuthService.authorization(request, response, cookieClientID, cookieRole)) {
            response.sendRedirect(loginPage);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getParameter("email") != null &&
                request.getParameter("password") != null) {

            String email = request.getParameter("email");
            String password = request.getParameter("password");
            int clientID = Token.getClientID(email);

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

            Cookie cookieAccessToken = new Cookie("token", redirecter.getAccessToken().trim());
            Cookie cookieRole = new Cookie("role", redirecter.getRole().trim());
            Cookie cookieClientID = new Cookie("clientID", Integer.toString(clientID));

            cookieAccessToken.setPath("/");
            cookieAccessToken.setMaxAge(20);

            cookieRole.setPath("/");
            cookieRole.setMaxAge(18000);

            cookieClientID.setPath("/");
            cookieClientID.setMaxAge(18000);

            response.addCookie(cookieAccessToken);
            response.addCookie(cookieRole);
            response.addCookie(cookieClientID);

            try {
                response.sendRedirect(redirecter.getUrl());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
