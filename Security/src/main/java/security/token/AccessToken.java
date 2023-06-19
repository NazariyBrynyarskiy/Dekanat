package security.token;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import dao.Role;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;


public class AccessToken extends Token {

    private String accessToken;
    private final String secretKey;


    public AccessToken() {
        accessToken = null;
        secretKey = initializeKey("Access");
    }


    public void createAccessToken(String email, String password, String role) throws JOSEException, ParseException {
        String subject = "HS256";

        JWSSigner signer = new MACSigner(secretKey.getBytes());

        JWTClaimsSet.Builder claimsSetBuilder = new JWTClaimsSet.Builder();
        claimsSetBuilder.subject(subject);
        claimsSetBuilder.claim("role", role);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 2);
        claimsSetBuilder.expirationTime(calendar.getTime());

        claimsSetBuilder.claim("email", email);
        claimsSetBuilder.claim("password", password);

        JWTClaimsSet claimsSet = claimsSetBuilder.build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(signer);

        accessToken = signedJWT.serialize();
    }


//    public boolean createAccessToken(HttpServletResponse response, String role) throws ParseException, JOSEException {
//        if (accessToken != null && !isValidToken(accessToken, secretKey)) {
//            RefreshToken refreshToken = new RefreshToken();
//            if (refreshToken.isValidToken(refreshToken.getRefreshToken(), refreshToken.getSecretKey())) {
//                Role clientRole = new Role();
//                try {
//                    String email = decodeToken(refreshToken.getRefreshToken()).email();
//                    String password = decodeToken(refreshToken.getRefreshToken()).password();
//                    createAccessToken(email, password, clientRole.getRole(email, password));
//
//                    Cookie cookieAccessToken = new Cookie("token", getAccessToken());
//                    Cookie cookieRole = new Cookie("role", role);
//                    cookieAccessToken.setPath("/");
//                    cookieAccessToken.setMaxAge(120);
//                    cookieRole.setPath("/");
//                    cookieRole.setMaxAge(120);
//                    response.addCookie(cookieAccessToken);
//                    response.addCookie(cookieRole);
//                } catch (SQLException e) {
//                    throw new RuntimeException(e);
//                }
//            } else {
//                return false;
//            }
//        }
//        return true;
//    }


    public String getAccessToken() {
        return accessToken;
    }


    public String getSecretKey() {
        return secretKey;
    }

}
