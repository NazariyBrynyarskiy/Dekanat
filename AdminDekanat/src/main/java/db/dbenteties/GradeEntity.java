package db.dbenteties;

public record GradeEntity(String subjectName, int dekanatID, int grade) {

    public GradeEntity(String subjectName, int dekanatID, int grade) {
        this.subjectName = subjectName;
        this.dekanatID = dekanatID;
        this.grade = grade;
    }

}
