package managers.grades;

import db.studentgrades.GradeEntity;
import db.studentgrades.GradesDBAccess;
import managers.grades.checker.Check;
import managers.grades.checker.checkings.PositiveGrade;
import managers.grades.checker.checkings.StudentExisting;
import managers.grades.checker.checkings.SubjectExisting;

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

    public boolean insert(String subject, int grade, int dekanatID) throws SQLException {
        List<Check> checks = new ArrayList<>();
        checks.add(new StudentExisting());
        checks.add(new SubjectExisting());
        checks.add(new PositiveGrade());


        if ( !(checks.get(0).check(dekanatID) ||
                checks.get(1).check(subject) ||
                checks.get(2).check(grade)) ) {
            return false;
        }

        gradesDBAccess.insert(subject, grade, dekanatID);

        return true;
    }
}
