package db.dbenteties;

public record FacultyEntity(int idFaculty, String facultyName) {

    public FacultyEntity(int idFaculty, String facultyName) {
        this.idFaculty = idFaculty;
        this.facultyName = facultyName;
    }
}
