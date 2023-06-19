package lecturermanagers;

import lecturerdb.dbaccess.StudentDBAccess;
import lecturerdb.dbenteties.StudentEntity;

import java.sql.SQLException;
import java.util.List;

public class StudentManager {
    private final List<StudentEntity> studentEntities;

    public StudentManager() throws SQLException {
        StudentDBAccess studentDBAccess = new StudentDBAccess();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        studentEntities = studentDBAccess.select();
    }

    public List<StudentEntity> getStudentEntities() {
        return studentEntities;
    }
}
