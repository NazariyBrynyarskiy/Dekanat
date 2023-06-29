package lecturerdb.dbaccess;

import com.nimbusds.jose.JOSEException;
import lecturerdb.dbaccess.interfaces.SelectFromDB;
import lecturerdb.dbenteties.GradeEntity;
import security.AuthService;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class GradesDBAccess extends lecturerdb.dbaccess.interfaces.Connection implements SelectFromDB<List<GradeEntity>> {

    @Override
    public List<GradeEntity> select(String token) throws SQLException {
        List<GradeEntity> list = new ArrayList<>();

        String subjectName;
        int grade;
        int dekanatID;

        try (java.sql.Connection connection = DriverManager.getConnection(
                getCONNECTION(), "newuser", "password");
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM Grades" +
                             " JOIN Students ON Grades.dekanatID = Students.dekanatID" +
                             " JOIN `Groups` ON Students.groupName = `Groups`.groupName " +
                             " JOIN Specialities ON `Groups`.idSpeciality = Specialities.idSpeciality" +
                             " JOIN Faculties ON Specialities.idFaculty = Faculties.idFaculty" +
                             " WHERE facultyName = ?")) {
            preparedStatement.setString(1, getLecturerFaculty(token));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    subjectName = resultSet.getString("subjectName");
                    dekanatID = resultSet.getInt("dekanatID");
                    grade = resultSet.getInt("grade");

                    list.add(new GradeEntity(subjectName, dekanatID, grade));
                }
            }
        } catch (SQLException | ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public void insert(String subjectName, int dekanatID, int grade) {
        try (java.sql.Connection connection = DriverManager.getConnection(
                getCONNECTION(), "newuser", "password");
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO Grades (subjectName, dekanatID, grade) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, subjectName);
            preparedStatement.setInt(2, dekanatID);
            preparedStatement.setInt(3, grade);

            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
