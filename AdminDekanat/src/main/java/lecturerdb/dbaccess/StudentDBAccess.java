package lecturerdb.dbaccess;

import com.nimbusds.jose.JOSEException;
import lecturerdb.dbaccess.interfaces.SelectFromDB;
import lecturerdb.dbenteties.StudentEntity;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class StudentDBAccess extends lecturerdb.dbaccess.interfaces.Connection implements SelectFromDB<List<StudentEntity>> {

    @Override
    public List<StudentEntity> select(String token) throws SQLException {
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
                     "SELECT *" +
                             " FROM Students" +
                             " JOIN `Groups` ON Students.groupName = `Groups`.groupName" +
                             " JOIN Specialities ON `Groups`.idSpeciality = Specialities.idSpeciality" +
                             " JOIN Faculties ON Specialities.idFaculty = Faculties.idFaculty" +
                             " WHERE Faculties.facultyName = ?")) {
            try {
                preparedStatement.setString(1, getLecturerFaculty(token));
            } catch (ParseException | JOSEException e) {
                throw new RuntimeException(e);
            }
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

    public String getStudentFaculty(int dekanatID) {
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

    public int checkStudentExisting(int dekanatID) {
        int studentID = 0;
        try (java.sql.Connection connection = DriverManager.getConnection(
                getCONNECTION(), "newuser", "password");
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
