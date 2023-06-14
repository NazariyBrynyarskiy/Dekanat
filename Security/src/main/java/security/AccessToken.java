package security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;
import java.util.Calendar;


public class AccessToken extends Token {

    private String accessToken;
    private final String secretKey;


    protected AccessToken() {
        accessToken = null;
        secretKey = initializeKey("Access");
    }


    protected void createAccessToken(String email, String password, String role) throws JOSEException, ParseException {
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


    protected String getAccessToken() {
        return accessToken;
    }


    protected String getSecretKey() {
        return secretKey;
    }

}
