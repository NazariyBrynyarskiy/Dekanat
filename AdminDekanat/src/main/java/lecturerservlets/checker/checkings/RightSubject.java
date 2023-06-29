package lecturerservlets.checker.checkings;

import com.nimbusds.jose.JOSEException;
import lecturerdb.dbaccess.SubjectDBAccess;
import lecturerdb.dbaccess.interfaces.SelectFromDB;
import lecturerservlets.checker.CheckTwoAttributes;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class RightSubject implements CheckTwoAttributes<String> {

    public boolean check(String token, String subjectName) {
        SelectFromDB<List<String>> getSubjectsFromDB = new SubjectDBAccess();
        try {
            List<String> subjects = new ArrayList<>(getSubjectsFromDB.select(token));
            for (String subject : subjects) {
                if (subjectName.equals(subject)) {
                    return true;
                }
            }
        } catch (SQLException | ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
}
