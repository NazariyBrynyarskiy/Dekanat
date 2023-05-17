package managers.grades.checker.checkings;

import db.dbaccess.StudentDBAccess;
import managers.grades.checker.Check;

import java.sql.SQLException;

public class StudentExisting implements Check<Integer> {
    @Override
    public boolean check(Integer dekanatID) {
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
