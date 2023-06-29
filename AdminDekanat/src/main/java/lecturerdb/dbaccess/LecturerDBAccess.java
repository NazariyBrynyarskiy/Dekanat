package lecturerdb.dbaccess;

import com.nimbusds.jose.JOSEException;
import lecturerdb.dbaccess.interfaces.Connection;
import lecturerdb.dbaccess.interfaces.SelectFromDB;
import lecturerdb.dbenteties.LecturerEntity;
import security.AuthService;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

public class LecturerDBAccess extends lecturerdb.dbaccess.interfaces.Connection implements SelectFromDB<LecturerEntity> {


    @Override
    public LecturerEntity select(String token) throws SQLException, ParseException, JOSEException {
        int idLecturer = AuthService.getIdLecturer(token);
        String name = "";
        String surname = "";

        try(java.sql.Connection connection =
                    DriverManager.getConnection(getCONNECTION(), "newuser", "password");
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT name, surname FROM Lecturers WHERE idLecturer = ?")) {
            preparedStatement.setInt(1, idLecturer);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    name = resultSet.getString("name");
                    surname = resultSet.getString("surname");
                }
            }
        }
        return new LecturerEntity(name, surname);
    }
}
