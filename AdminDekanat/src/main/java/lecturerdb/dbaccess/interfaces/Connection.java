package lecturerdb.dbaccess.interfaces;

import com.nimbusds.jose.JOSEException;
import security.AuthService;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

abstract public class Connection {
    private final String CONNECTION;

    protected Connection() {
        CONNECTION = "jdbc:mysql://localhost:3306/Dekan";
    }

    protected String getCONNECTION() { return CONNECTION; }

    public String getLecturerFaculty(String token) throws ParseException, JOSEException {
        int idLecturer = AuthService.getIdLecturer(token);
        String facultyName = "";
        try (java.sql.Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/Dekan", "newuser", "password");
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT facultyName FROM Lecturers" +
                             " JOIN " +
                             " Faculties ON Lecturers.idFaculty = Faculties.idFaculty WHERE idLecturer = ?")) {
            preparedStatement.setInt(1, idLecturer);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    facultyName = resultSet.getString("facultyName");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return facultyName;
    }

}
