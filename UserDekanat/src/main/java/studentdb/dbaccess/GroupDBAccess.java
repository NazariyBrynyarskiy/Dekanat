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

public class GroupDBAccess extends Connection implements SelectFromDB<String> {

    @Override
    public String select(String token) throws ParseException, JOSEException {
        int dekanatID = AuthService.getDekanatID(token);
        String groupName = null;

        try (java.sql.Connection connection = DriverManager.getConnection(
                getCONNECTION(), "newuser", "password");
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT groupName FROM Students WHERE dekanatID = ?")) {
            preparedStatement.setInt(1, dekanatID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    groupName = resultSet.getString("groupName");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return groupName;
    }
}
