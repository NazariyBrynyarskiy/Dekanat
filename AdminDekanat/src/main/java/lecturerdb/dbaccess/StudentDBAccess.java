package lecturerdb.dbaccess;

import lecturerdb.dbenteties.StudentEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDBAccess extends Connection {
    public StudentDBAccess() {

    }

    public List<StudentEntity> select() throws SQLException {
        List<StudentEntity> list = new ArrayList<>();

        int dekanatID;
        String name;
        String surname;
        String groupName;
        String formOfEducation;
        String formOfPayment;

        try (java.sql.Connection connection = DriverManager.getConnection(
                getCONNECTION(), "newuser", "password");
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM Students")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    dekanatID = resultSet.getInt("dekanatID");
                    name = resultSet.getString("name");
                    surname = resultSet.getString("surname");
                    groupName = resultSet.getString("groupName");
                    formOfEducation = resultSet.getString("formOfEducation");
                    formOfPayment = resultSet.getString("formOfPayment");

                    list.add(new StudentEntity(dekanatID, name, surname, groupName, formOfEducation, formOfPayment));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public int getStudentID(int dekanatID) throws SQLException {
        int studentID = 0;
        try (java.sql.Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/Dekan", "newuser", "password");
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT dekanatID FROM Students WHERE dekanatID = ?")) {
            preparedStatement.setInt(1, dekanatID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    studentID = resultSet.getInt("dekanatID");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return studentID;
    }

}
