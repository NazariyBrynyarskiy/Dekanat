package security.token;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.Cookie;
import security.AuthService;

import java.sql.*;
import java.text.ParseException;
import java.util.Calendar;


public class RefreshToken extends Token {

    private String refreshToken;
    private final String secretKey;


    public RefreshToken() {
        secretKey = initializeKey("Refresh");
    }

    public RefreshToken(int clientID, String role) {
        refreshToken = initializeToken(clientID, role);
        secretKey = initializeKey("Refresh");
    }


    private String initializeToken(int clientID, String role) {
        try (java.sql.Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/Tokens", "newuser", "password");
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT `refreshToken` FROM RefreshTokens WHERE clientID = ? AND role = ?")) {
            preparedStatement.setInt(1, clientID);
            preparedStatement.setString(2, role);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("refreshToken");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public void createRefreshToken(String email, String password, String role) throws JOSEException {
        String subject = "HS256";

        JWSSigner signer = new MACSigner(secretKey.getBytes());

        JWTClaimsSet.Builder claimsSetBuilder = new JWTClaimsSet.Builder();
        claimsSetBuilder.subject(subject);
        claimsSetBuilder.claim("role", role);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 5);
        claimsSetBuilder.expirationTime(calendar.getTime());

        claimsSetBuilder.claim("email", email);
        claimsSetBuilder.claim("password", password);

        JWTClaimsSet claimsSet = claimsSetBuilder.build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(signer);

        refreshToken = signedJWT.serialize();
        updateRefreshToken(email, refreshToken);
    }


    private void updateRefreshToken(String email, String token) {
        try (java.sql.Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/Tokens", "newuser", "password");
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE RefreshTokens SET refreshToken = ? WHERE clientID = ? AND role = ?")) {
            preparedStatement.setString(1, token);
            preparedStatement.setInt(2, getClientID(email));
            preparedStatement.setString(3, getClientRole(email));

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public String getRefreshToken() {
        return refreshToken;
    }

    public String getSecretKey() { return secretKey; }

    //    public static void main(String[] args) throws SQLException, JOSEException {
//        RefreshToken refreshToken = new RefreshToken();
//        System.out.println(refreshToken.isValidToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIUzI1NiIsInBhc3N3b3JkIjoiUXViSWo4S2QxIiwicm9sZSI6InVzZXIiLCJleHAiOjE2ODY3NTczMDcsImVtYWlsIjoiYm9iYUBnbWFpbC5jb20ifQ.350nt_zq3MdIu_pBfmAGRIfcN26y_uFCxu7N32_-nug", refreshToken.secretKey));
////        refreshToken1.createRefreshToken("boba@gmail.com", "QubIj8Kd1", "user");
////        List<TokenRefresh> list = new ArrayList<>();
////        try (java.sql.Connection connection = DriverManager.getConnection(
////                "jdbc:mysql://localhost:3306/Dekan", "newuser", "password");
////             PreparedStatement preparedStatement = connection.prepareStatement("SELECT dekanatID FROM Students")) {
////
////            try (ResultSet resultSet = preparedStatement.executeQuery()) {
////                while (resultSet.next()) {
////                    list.add(new TokenRefresh(resultSet.getInt("dekanatID"), "user"));
////                }
////            }
////        } catch (SQLException e) {
////            throw new RuntimeException(e);
////        }
////
////        try (java.sql.Connection connection = DriverManager.getConnection(
////                "jdbc:mysql://localhost:3306/Dekan", "newuser", "password");
////             PreparedStatement preparedStatement = connection.prepareStatement(
////                     "SELECT idLecturer FROM Lecturers")) {
////
////            try (ResultSet resultSet = preparedStatement.executeQuery()) {
////                while (resultSet.next()) {
////                    list.add(new TokenRefresh(resultSet.getInt("idLecturer"), "admin"));
////                }
////            }
////        } catch (SQLException e) {
////            throw new RuntimeException(e);
////        }
////
////        java.sql.Connection connection = DriverManager.getConnection(
////                "jdbc:mysql://localhost:3306/Tokens", "newuser", "password");
////        Statement statement = connection.createStatement();
////
////        for (TokenRefresh tokenn : list) {
////            statement.execute("INSERT INTO RefreshTokens (clientID, role) " +
////                    "VALUES (" + tokenn.id + ", '" + tokenn.role + "')");
////        }
////
////        connection.close();
////        statement.close();
//
//    }

    public static void main(String[] args) throws ParseException, JOSEException {
        String t = null;
        if (t == null) {
            RefreshToken refreshToken = new RefreshToken(1, "user");
            if (refreshToken.isValidToken(refreshToken.getRefreshToken(), refreshToken.getSecretKey())) {
                String email = refreshToken.decodeToken(refreshToken.getRefreshToken(), refreshToken.getSecretKey()).email();
                String password = refreshToken.decodeToken(refreshToken.getRefreshToken(), refreshToken.getSecretKey()).password();
                String role = refreshToken.decodeToken(refreshToken.getRefreshToken(), refreshToken.getSecretKey()).role();
                System.out.println(email + ", " + password + ", " + role);
            } else {
                System.out.println("zalupa");
            }
        }
    }

}
