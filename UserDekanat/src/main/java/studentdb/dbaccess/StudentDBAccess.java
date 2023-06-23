package studentdb.dbaccess;

import com.nimbusds.jose.JOSEException;
import security.AuthService;
import studentdb.dbaccess.interfaces.Connection;
import studentdb.dbaccess.interfaces.SelectFromDB;
import studentdb.dbentities.StudentEntity;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

public class StudentDBAccess extends Connection implements SelectFromDB<StudentEntity> {

    @Override
    public StudentEntity select(String token) throws ParseException, JOSEException {
        int dekanatID = AuthService.getDekanatID(token);
        String surname = null;
        String name = null;

        try (java.sql.Connection connection = DriverManager.getConnection(
                getCONNECTION(), "newuser", "password");
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT name, surname FROM Students WHERE dekanatID = ?")) {
            preparedStatement.setInt(1, dekanatID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    surname = resultSet.getString("surname");
                    name = resultSet.getString("name");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new StudentEntity(surname, name);
    }
}
