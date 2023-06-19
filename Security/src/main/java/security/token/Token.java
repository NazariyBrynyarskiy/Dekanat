package security.token;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import security.Client;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

public abstract class Token {

    public boolean isValidToken(String token, String secretKey) {
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


    public Client decodeToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        AccessToken accessToken = new AccessToken();
        JWSVerifier verifier = new MACVerifier(accessToken.getSecretKey().getBytes());
        String decodedEmail = null;
        String decodedPassword = null;

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
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Client(decodedEmail, decodedPassword);
    }

}
