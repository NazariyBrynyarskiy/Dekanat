package accountdb.access;

import accountdb.enteties.LecturerEntity;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LecturerDBAccess extends Connection implements Selector<LecturerEntity> {

    @Override
    public LecturerEntity select(String email, String password) throws SQLException {
        LecturerEntity lecturerEntity = null;
        int idLecturer;
        String name, surname;
        int idFaculty;

        java.sql.Connection connection =
                DriverManager.getConnection(getCONNECTION(), "newuser", "password");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Lecturers " +
                "WHERE email = '" + email + "' AND password = '" + password + "'");
        while (resultSet.next()) {
            idLecturer = resultSet.getInt("idLecturer");
            name = resultSet.getString("name");
            surname = resultSet.getString("surname");
            idFaculty = resultSet.getInt("idFaculty");

            lecturerEntity = new LecturerEntity(idLecturer, name, surname, idFaculty);
        }

        connection.close();
        statement.close();
        resultSet.close();

        return lecturerEntity;
    }

}
