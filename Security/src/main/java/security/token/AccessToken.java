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


    public boolean createAccessToken(HttpServletResponse response) throws ParseException, JOSEException {
        AccessToken accessToken = new AccessToken();
        Token decodeAccessToken = new AccessToken();
        if (accessToken.getAccessToken() != null &&
                !accessToken.isValidToken(accessToken.getAccessToken(), accessToken.getSecretKey())) {
            RefreshToken refreshToken = new RefreshToken();
            if (refreshToken.isValidToken(refreshToken.getRefreshToken(), refreshToken.getSecretKey())) {
                String email = decodeAccessToken.decodeToken(refreshToken.getRefreshToken()).email();
                String password = decodeAccessToken.decodeToken(refreshToken.getRefreshToken()).password();
                String role = decodeAccessToken.decodeToken(refreshToken.getRefreshToken()).role();
                accessToken.createAccessToken(email, password, role);
                Cookie cookieAccessToken = new Cookie("token", getAccessToken());
                Cookie cookieRole = new Cookie("role", role);
                cookieAccessToken.setPath("/");
                cookieAccessToken.setMaxAge(120);
                cookieRole.setPath("/");
                cookieRole.setMaxAge(120);
                response.addCookie(cookieAccessToken);
                response.addCookie(cookieRole);
            } else {
                return false;
            }
        }
        return true;
    }


    public String getAccessToken() {
        return accessToken;
    }


    public String getSecretKey() {
        return secretKey;
    }

}
