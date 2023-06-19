package lecturermanagers.checker.checkings;

import lecturermanagers.checker.Check;

public class PositiveGrade implements Check {

    @Override
    public boolean check(int grade) {
        return grade >= 0;
    }
}
