package security;

import dao.Role;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import security.token.AccessToken;
import security.token.RefreshToken;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

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


    public static boolean authorization(HttpServletRequest request, HttpServletResponse response, int clientID, String role) throws ParseException, JOSEException {
        AccessToken accessToken = new AccessToken(request);
        if (!accessToken.createAccessToken(response, clientID, role)) {
            return false;
        }

        accessToken.createAccessToken(response, clientID, role);

        String decodedEmail = accessToken.decodeToken(accessToken.getAccessToken(), accessToken.getSecretKey()).email();
        String decodedPassword = accessToken.decodeToken(accessToken.getAccessToken(), accessToken.getSecretKey()).password();

        if (role.equals("user")) {
            try (java.sql.Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/Dekan", "newuser", "password");
                 PreparedStatement preparedStatement = connection.prepareStatement(
                         "SELECT * FROM Students WHERE email = ? AND `password` = ?")) {
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
        } else {
            try (java.sql.Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/Dekan", "newuser", "password");
                 PreparedStatement preparedStatement = connection.prepareStatement(
                         "SELECT * FROM Lecturers WHERE email = ? AND `password` = ?")) {
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
        }
        return false;
    }


    public static int getDekanatID(String token) throws ParseException, JOSEException {
        AccessToken accessToken = new AccessToken();
        Client client = accessToken.decodeToken(token, accessToken.getSecretKey());
        int dekanatID = 0;
        try (java.sql.Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/Dekan", "newuser", "password");
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT dekanatID FROM Students WHERE email = ? AND password = ?")) {
            preparedStatement.setString(1, client.email());
            preparedStatement.setString(2, client.password());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
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
