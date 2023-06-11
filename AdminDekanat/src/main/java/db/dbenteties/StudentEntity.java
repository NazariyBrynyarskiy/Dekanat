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

}
