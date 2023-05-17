package db.dbenteties;

public record StudentEntity(int dekanatID,
                            String name,
                            String surname,
                            String groupName,
                            String formOfEducation,
                            String formOfPayment) {

    public StudentEntity(int dekanatID,
                         String name,
                         String surname,
                         String groupName,
                         String formOfEducation,
                         String formOfPayment) {
        this.dekanatID = dekanatID;
        this.name = name;
        this.surname = surname;
        this.groupName = groupName;
        this.formOfEducation = formOfEducation;
        this.formOfPayment = formOfPayment;
    }

    @Override
    public String toString() {
        return "{ Dekanat id: " + dekanatID +
                ", Name: " + name +
                ", Surname: "+ surname +
                ", Id group: " + groupName +
                ", Form of education: " + formOfEducation +
                ", Form of payment: " + formOfPayment +
                " }";
    }
}
