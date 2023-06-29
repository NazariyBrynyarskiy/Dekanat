package lecturerdb.dbaccess;

import com.nimbusds.jose.JOSEException;
import lecturerdb.dbaccess.interfaces.Connection;
import lecturerdb.dbaccess.interfaces.SelectFromDB;
import security.AuthService;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class SubjectDBAccess extends Connection implements SelectFromDB<List<String>> {

    @Override
    public List<String> select(String token) throws SQLException, ParseException, JOSEException {
        List<String> subjects = new ArrayList<>();
        String lecturerFaculty = getLecturerFaculty(token);

        try (java.sql.Connection connection = DriverManager.getConnection(
                getCONNECTION(), "newuser", "password");
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT subjectName FROM Subjects" +
                             " JOIN" +
                             "  Faculties ON Subjects.idFaculty = Faculties.idFaculty WHERE facultyName = ?")) {
            preparedStatement.setString(1, lecturerFaculty);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    subjects.add(resultSet.getString("subjectName"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return subjects;
    }
}
