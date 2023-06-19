package lecturerdb.dbenteties;

public record SubjectEntity(String subjectName, int idFaculty) {

    public SubjectEntity(String subjectName, int idFaculty) {
        this.subjectName = subjectName;
        this.idFaculty = idFaculty;
    }
}
