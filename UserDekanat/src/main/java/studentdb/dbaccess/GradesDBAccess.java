package studentdb.dbaccess;

import com.nimbusds.jose.JOSEException;
import security.AuthService;
import studentdb.dbaccess.interfaces.SelectFromDB;
import studentdb.dbentities.GradeEntity;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class GradesDBAccess extends studentdb.dbaccess.interfaces.Connection implements SelectFromDB<List<GradeEntity>> {

    @Override
    public List<GradeEntity> select(String token) throws ParseException, JOSEException {
        List<GradeEntity> list = new ArrayList<>();
        int dekanatID = AuthService.getDekanatID(token);

        String subjectName;
        int grade;

        try (java.sql.Connection connection = DriverManager.getConnection(
                getCONNECTION(), "newuser", "password");
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT subjectName, grade FROM Grades WHERE dekanatID = ?")) {
            preparedStatement.setInt(1, dekanatID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    subjectName = resultSet.getString("subjectName");
                    grade = resultSet.getInt("grade");

                    list.add(new GradeEntity(subjectName, grade));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }
}
