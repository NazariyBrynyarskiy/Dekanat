package lecturerdb.dbenteties;

public record StudentEntity(int dekanatID,
                            String name,
                            String surname,
                            String groupName,
                            String formOfEducation,
                            String formOfPayment) {
}
