package lecturerservlets;

import jakarta.servlet.http.HttpServletRequest;
import lecturerdb.dbaccess.StudentDBAccess;
import lecturerdb.dbenteties.StudentEntity;

import java.sql.SQLException;
import java.util.List;

public class StudentManager {

    public StudentManager() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<StudentEntity> getStudentEntities(HttpServletRequest request) {
        CookiesController controller = new CookiesController();
        StudentDBAccess studentDBAccess = new StudentDBAccess();
        try {
            return studentDBAccess.select(controller.getToken(request));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
