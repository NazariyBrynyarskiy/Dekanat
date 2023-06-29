package lecturerservlets.checker.checkings;

import lecturerservlets.checker.CheckOneAttribute;

public class PositiveGrade implements CheckOneAttribute {

    @Override
    public boolean check(int grade) {
        return grade >= 0;
    }
}
