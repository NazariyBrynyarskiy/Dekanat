package studentdb.dbaccess;

import com.nimbusds.jose.JOSEException;
import security.AuthService;
import studentdb.dbaccess.interfaces.Connection;
import studentdb.dbaccess.interfaces.SelectFromDB;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

public class FacultyDBAccess extends Connection implements SelectFromDB<String> {

    @Override
    public String select(String token) throws ParseException, JOSEException {
        int dekanatID = AuthService.getDekanatID(token);
        String faculty = null;

        try (java.sql.Connection connection = DriverManager.getConnection(
                getCONNECTION(), "newuser", "password");
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT facultyName FROM Students" +
                             " JOIN" +
                             " `Groups` ON Students.groupName = `Groups`.groupName" +
                             " JOIN" +
                             "  Specialities ON `Groups`.idSpeciality = Specialities.idSpeciality" +
                             " JOIN" +
                             "  Faculties ON Specialities.idFaculty = Faculties.idFaculty WHERE dekanatID = ?")) {
            preparedStatement.setInt(1, dekanatID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    faculty = resultSet.getString("facultyName");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return faculty;
    }
}
