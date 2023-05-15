package db.studentinfo;

public class StudentEntity {
    private final String name;
    private final String surname;
    private final String faculty;
    private final String studyingStart;
    private final String group;
    private final String formOfEducation;
    private final String trainingDirection;
    private final String formOfPayment;

    public StudentEntity(String name,
                         String surname,
                         String faculty,
                         String studyingStart,
                         String group,
                         String formOfEducation,
                         String trainingDirection,
                         String formOfPayment) {
        this.name = name;
        this.surname = surname;
        this.faculty = faculty;
        this.studyingStart = studyingStart;
        this.group = group;
        this.formOfEducation = formOfEducation;
        this.trainingDirection = trainingDirection;
        this.formOfPayment = formOfPayment;
    }

    @Override
    public String toString() {
        return "{" + name + ", "
                   + surname +
                ", Faculty: " + faculty +
                ", Studying start: " + studyingStart +
                ", Group: " + group +
                ", Form of education: " + formOfEducation +
                ", Training direction: " + trainingDirection +
                ", Form of payment: " + formOfPayment +
                "}";
    }
}
