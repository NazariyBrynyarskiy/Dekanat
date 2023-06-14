package security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

public abstract class Token {

     protected boolean isValidToken(String token, String secretKey) {
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

    protected String initializeKey(String type) {
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
}
