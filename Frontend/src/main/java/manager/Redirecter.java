package manager;

import accountdb.access.LecturerDBAccess;
import accountdb.access.Selector;
import accountdb.access.StudentDBAccess;
import accountdb.enteties.LecturerEntity;
import accountdb.enteties.StudentEntity;

import java.sql.SQLException;

public class Redirecter {
    private final String email;
    private final String password;

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
        Selector<LecturerEntity> lecturerEntitySelector = new LecturerDBAccess();
        Selector<StudentEntity> studentEntitySelector = new StudentDBAccess();

        if (lecturerEntitySelector.select(email, password) != null) {
            return "http://localhost:8080/admin/index";
        }
        if (studentEntitySelector.select(email, password) != null) {
            return "http://localhost:8080/user/index";
        }

        return "http://localhost:8080";
    }
}
