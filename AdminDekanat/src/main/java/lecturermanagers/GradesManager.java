package lecturermanagers;

import lecturerdb.dbenteties.GradeEntity;
import lecturerdb.dbaccess.GradesDBAccess;
import lecturermanagers.checker.Check;
import lecturermanagers.checker.checkings.PositiveGrade;
import lecturermanagers.checker.checkings.StudentExisting;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GradesManager {
    private final List<GradeEntity> gradeEntities;
    private GradesDBAccess gradesDBAccess;

    public GradesManager() throws SQLException {
        gradesDBAccess = new GradesDBAccess();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        gradeEntities = gradesDBAccess.select();
    }

    public List<GradeEntity> getGradeEntities() {
        return gradeEntities;
    }

    private boolean checker(int grade, int dekanatID) {
        List<Check> checks = new ArrayList<>();
        checks.add(new StudentExisting());
        checks.add(new PositiveGrade());

        if ( !checks.get(0).check(dekanatID) ||
                !checks.get(1).check(grade) ) {
            return false;
        }

        return true;
    }

    public void insert(String subjectName, int dekanatID, int grade) throws SQLException {
        if (checker(grade, dekanatID)) {
            gradesDBAccess.insert(subjectName, dekanatID, grade);
        }
    }
}
