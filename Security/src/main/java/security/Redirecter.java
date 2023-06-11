package security;

import dao.Role;
import com.nimbusds.jose.JOSEException;

import java.sql.SQLException;
import java.text.ParseException;

public class Redirecter {
    private final String email;
    private final String password;
    private String accessToken;
    private String role;

    public Redirecter(String email, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        this.email = email;
        this.password = password;
    }

    public String url() throws SQLException {
        Role getRole = new Role();
        role = getRole.getRole(email, password);

        if (role != null) {
            AccessToken access = new AccessToken();
            try {
                access.createToken(email, password, role);
                accessToken = access.getAccessToken();
            } catch (JOSEException | ParseException e) {
                throw new RuntimeException(e);
            }
            return "http://localhost:8080/" + role + "/index";
        }

        return "http://localhost:8080";
    }


    public String getAccessToken() {
        return accessToken;
    }

    public String getRole() {
        return role;
    }

}
