package security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

public class AccessToken {

    private String accessToken;
    private final String secretKey;

    public AccessToken() {
        accessToken = null;
        secretKey = "BKOmfGcv2mqFtywVU8eOS8FTLns3issu0SrbfWooY7M";
    }


    public void createToken(String email, String password, String role) throws JOSEException, ParseException {
        String subject = "HS256";

        JWSSigner signer = new MACSigner(secretKey.getBytes());

        JWTClaimsSet.Builder claimsSetBuilder = new JWTClaimsSet.Builder();
        claimsSetBuilder.subject(subject);
        claimsSetBuilder.claim("role", role);
        claimsSetBuilder.expirationTime(new Date(new Date().getTime() + 60 * 1000)); // Token expiration time (1 minute from now)
        claimsSetBuilder.claim("email", email);
        claimsSetBuilder.claim("password", password);

        JWTClaimsSet claimsSet = claimsSetBuilder.build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(signer);

        accessToken = signedJWT.serialize();
    }

    public boolean authorization(String token, String role) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(secretKey.getBytes());
        String decodedEmail = null;
        String decodedPassword = null;
        String decodedRole = null;


        if (signedJWT.verify(verifier)) {
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            decodedEmail = (String) claimsSet.getClaim("email");
            decodedPassword = (String) claimsSet.getClaim("password");
            decodedRole = (String) claimsSet.getClaim("role");
        }

        if (!Objects.equals(role, decodedRole)) {
            return false;
        }

        if (role.equals("user")) {
            role = "Students";
        }
        else if (role.equals("admin")) {
            role = "Lecturers";
        }

        try (java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Dekan", "newuser", "password");
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + role + " WHERE email = ? AND password = ?")) {
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



    public boolean authentication(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(secretKey.getBytes());
        String decodedEmail = null;
        String decodedPassword = null;

        if (signedJWT.verify(verifier)) {
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            decodedEmail = (String) claimsSet.getClaim("email");
            decodedPassword = (String) claimsSet.getClaim("password");
        }

        try (java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Dekan", "newuser", "password");
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT email, `password` FROM Students WHERE email = ? AND `password` = ? " +
                     " union all" +
                     " SELECT email, `password` FROM Lecturers WHERE email = ? AND `password` = ?")) {
            preparedStatement.setString(1, decodedEmail);
            preparedStatement.setString(2, decodedPassword);
            preparedStatement.setString(3, decodedEmail);
            preparedStatement.setString(4, decodedPassword);

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

    public String getAccessToken() {
        return accessToken;
    }

}
