package managers.checker.checkings;

import managers.checker.Check;

public class PositiveGrade implements Check {

    @Override
    public boolean check(int grade) {
        return grade >= 0;
    }
}
