package lecturerservlets.checker.checkings;

import com.nimbusds.jose.JOSEException;
import lecturerdb.dbaccess.LecturerDBAccess;
import lecturerdb.dbaccess.StudentDBAccess;
import lecturerdb.dbaccess.interfaces.Connection;
import lecturerservlets.checker.CheckTwoAttributes;

import java.text.ParseException;

public class RightFaculty implements CheckTwoAttributes<Integer> {

    @Override
    public boolean check(String token, Integer dekanatID) {
        Connection lecturerDBAccess = new LecturerDBAccess();
        StudentDBAccess studentDBAccess = new StudentDBAccess();

        try {
            return lecturerDBAccess.getLecturerFaculty(token)
                    .equals(studentDBAccess.getStudentFaculty(dekanatID));
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }
    }

}
