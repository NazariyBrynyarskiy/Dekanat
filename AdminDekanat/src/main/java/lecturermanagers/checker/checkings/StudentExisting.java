package lecturermanagers.checker.checkings;

import lecturerdb.dbaccess.StudentDBAccess;
import lecturermanagers.checker.Check;

import java.sql.SQLException;

public class StudentExisting implements Check {
    @Override
    public boolean check(int dekanatID) {
        StudentDBAccess studentDBAccess = new StudentDBAccess();
        int dbStudentID = 0;
        try {
            dbStudentID = studentDBAccess.getStudentID(dekanatID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return dekanatID == dbStudentID;
    }
}
