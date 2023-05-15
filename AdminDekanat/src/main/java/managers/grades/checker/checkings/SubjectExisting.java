package managers.grades.checker.checkings;

import managers.grades.checker.Check;

public class SubjectExisting implements Check<String> {
    @Override
    public boolean check(String subject) {
        String dbSubject = "";

        return subject.equals(dbSubject);
    }
}
