package accountdb.access;

import accountdb.enteties.StudentEntity;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StudentDBAccess extends Connection implements Selector<StudentEntity> {

    @Override
    public StudentEntity select(String email, String password) throws SQLException {
        StudentEntity studentEntity = null;
        int dekanatID;
        String name, surname, groupName;
        String formOfEducation, formOfPayment;

        java.sql.Connection connection =
                DriverManager.getConnection(getCONNECTION(), "newuser", "password");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Students " +
                "WHERE email = '" + email + "' AND password = '" + password + "'");
        while (resultSet.next()) {
            dekanatID = resultSet.getInt("dekanatID");
            name = resultSet.getString("name");
            surname = resultSet.getString("surname");
            groupName = resultSet.getString("groupName");
            formOfEducation = resultSet.getString("formOfEducation");
            formOfPayment = resultSet.getString("formOfPayment");

            studentEntity = new StudentEntity(dekanatID, name, surname, groupName, formOfEducation, formOfPayment);
        }

        connection.close();
        statement.close();
        resultSet.close();

        return studentEntity;
    }

}
