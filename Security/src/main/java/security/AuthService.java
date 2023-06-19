package security;

import dao.Role;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletResponse;
import security.token.AccessToken;
import security.token.RefreshToken;
import security.token.Token;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Objects;

public class AuthService {
    private final String email;
    private final String password;
    private String accessToken;
    private String role;


    public AuthService(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public String getUrl() throws SQLException {
        Role getRole = new Role();
        role = getRole.getRole(email, password);

        if (role != null) {
            AccessToken access = new AccessToken();
            RefreshToken refresh = new RefreshToken();
            try {
                access.createAccessToken(email, password, role);
                refresh.createRefreshToken(email, password, role);
                accessToken = access.getAccessToken();
            } catch (JOSEException | ParseException e) {
                throw new RuntimeException(e);
            }
            return "http://localhost:8080/" + role + "/index";
        }

        return "http://localhost:8080";
    }


    public boolean authorization(HttpServletResponse response, String token, String role) throws ParseException, JOSEException {
        AccessToken accessToken = new AccessToken();
        if (!accessToken.createAccessToken(response)) {
            return false;
        }

        String decodedEmail = accessToken.decodeToken(token).email();
        String decodedPassword = accessToken.decodeToken(token).password();
        String decodedRole = accessToken.decodeToken(token).role();

        if (!Objects.equals(role, decodedRole)) {
            return false;
        }

        if (role.equals("user")) { role = "Students"; }
        else if (role.equals("admin")) { role = "Lecturers"; }

        try (java.sql.Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/Dekan", "newuser", "password");
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM " + role + " WHERE email = ? AND password = ?")) {
            preparedStatement.setString(1, decodedEmail);
            preparedStatement.setString(2, decodedPassword);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }


    public boolean authentication(HttpServletResponse response, String token) throws ParseException, JOSEException {
        AccessToken accessToken = new AccessToken();
        if (!accessToken.createAccessToken(response)) {
            return false;
        }
        return accessToken.decodeToken(token) != null;
    }


    public int getDekanatID(String token) throws ParseException, JOSEException {
        Token accessToken = new AccessToken();
        Client client = accessToken.decodeToken(token);
        int dekanatID = 0;
        try (java.sql.Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/Dekan", "newuser", "password");
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT dekanatID FROM Students WHERE email = ? AND `password` = ?")) {
            preparedStatement.setString(1, client.email());
            preparedStatement.setString(2, client.password());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    dekanatID = resultSet.getInt("dekanatID");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dekanatID;
    }


    public String getAccessToken() {
        return accessToken;
    }


    public String getRole() {
        return role;
    }


}
