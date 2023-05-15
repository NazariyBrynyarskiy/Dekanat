package db.studentgrades;

public class GradeEntity {
    private final String subject;
    private final int grade;
    private final int dekanatID;

    public GradeEntity(String subject, int grade, int dekanatID) {
        this.subject = subject;
        this.grade = grade;
        this.dekanatID = dekanatID;
    }

    @Override
    public String toString() {
        return "{Subject: " + subject +
                ", Grade: " + grade +
                ", dekanatID: " + dekanatID +
                "}";
    }
}
