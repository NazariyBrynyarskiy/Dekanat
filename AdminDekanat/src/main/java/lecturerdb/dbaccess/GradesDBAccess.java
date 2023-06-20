package lecturerdb.dbaccess;

import lecturerdb.dbenteties.GradeEntity;
import lecturerdb.dbenteties.StudentEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradesDBAccess extends Connection {

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

    public List<GradeEntity> select() throws SQLException {
        List<GradeEntity> list = new ArrayList<>();

        String subjectName;
        int grade;
        int dekanatID;

        try (java.sql.Connection connection = DriverManager.getConnection(
                getCONNECTION(), "newuser", "password");
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM Grades")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    subjectName = resultSet.getString("subjectName");
                    dekanatID = resultSet.getInt("dekanatID");
                    grade = resultSet.getInt("grade");

                    list.add(new GradeEntity(subjectName, dekanatID, grade));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }
}
