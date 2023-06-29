package lecturerservlets.checker.checkings;

import lecturerdb.dbaccess.StudentDBAccess;
import lecturerservlets.checker.CheckOneAttribute;

public class StudentExisting implements CheckOneAttribute {

    @Override
    public boolean check(int dekanatID) {
        StudentDBAccess studentDBAccess = new StudentDBAccess();

        return studentDBAccess.checkStudentExisting(dekanatID) > 0;
    }
}
