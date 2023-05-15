package managers.grades.checker.checkings;

import managers.grades.checker.Check;

public class PositiveGrade implements Check<Integer> {

    @Override
    public boolean check(Integer grade) {
        return grade >= 0;
    }
}
