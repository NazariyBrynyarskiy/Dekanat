package security.token;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import dao.Role;
import security.Client;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

public abstract class Token {

    public boolean isValidToken(String token, String secretKey) {
        if (token == null) {
            return false;
        }
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secretKey.getBytes());

            if (signedJWT.verify(verifier)) {
                JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
                Date expirationTime = claimsSet.getExpirationTime();
                Date currentTime = new Date();
                boolean isExpired = (expirationTime != null && expirationTime.before(currentTime));

                return !isExpired;
            }
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public String initializeKey(String type) {
        try (java.sql.Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/Tokens", "newuser", "password");
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT `key` FROM TokenKeys WHERE type = ?")) {
            preparedStatement.setString(1, type);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("key");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static int getClientID(String email) {
        if (getClientRole(email).equals("user")) {
            try (java.sql.Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/Dekan", "newuser", "password");
                 PreparedStatement preparedStatement = connection.prepareStatement(
                         "SELECT dekanatID FROM Students WHERE email = ?")) {
                preparedStatement.setString(1, email);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("dekanatID");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try (java.sql.Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/Dekan", "newuser", "password");
                 PreparedStatement preparedStatement = connection.prepareStatement(
                         "SELECT idLecturer FROM Lecturers WHERE email = ?")) {
                preparedStatement.setString(1, email);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("idLecturer");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return 0;
    }


    public static String getClientRole(String email) {
        try (java.sql.Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/Dekan", "newuser", "password");
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT email FROM Lecturers WHERE email = ?")) {
            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return "admin";
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "user";
    }


    public Client decodeToken(String token, String key) throws ParseException, JOSEException {
        if (token == null) {
            return new Client("", "", "");
        }
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(key.getBytes());
        String decodedEmail = null;
        String decodedPassword = null;
        String clientRole = null;

        if (signedJWT.verify(verifier)) {
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            decodedEmail = (String) claimsSet.getClaim("email");
            decodedPassword = (String) claimsSet.getClaim("password");
        }

        try (java.sql.Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/Dekan", "newuser", "password");
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT email, `password` FROM Students WHERE email = ? AND `password` = ? " +
                             " UNION ALL " +
                             " SELECT email, `password` FROM Lecturers WHERE email = ? AND `password` = ?")) {
            preparedStatement.setString(1, decodedEmail);
            preparedStatement.setString(2, decodedPassword);
            preparedStatement.setString(3, decodedEmail);
            preparedStatement.setString(4, decodedPassword);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    decodedEmail = resultSet.getString("email");
                    decodedPassword = resultSet.getString("password");
                    Role role = new Role();
                    clientRole = role.getRole(decodedEmail, decodedPassword);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Client(decodedEmail, decodedPassword, clientRole);
    }

}
