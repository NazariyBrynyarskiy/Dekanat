package db.studentgrades;

import db.abstracts.Connection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradesDBAccess extends Connection {

    public void insert(String subject, int grade, int dekanatID) throws SQLException {
        java.sql.Connection connection = DriverManager.getConnection(getCONNECTION(), "newuser", "password");
        Statement statement = connection.createStatement();

        statement.execute("INSERT INTO Grades (subject, grade, dekanatID) " +
                "VALUES ('" + subject + "', " + grade + ", " + dekanatID + ")");

        connection.close();
        statement.close();

    }

    public List<GradeEntity> select() throws SQLException {
        List<GradeEntity> list = new ArrayList<>();

        String subject;
        int grade;
        int dekanatID;

        java.sql.Connection connection = DriverManager.getConnection(getCONNECTION(), "newuser", "password");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Grades");


        while (resultSet.next()) {
            subject = resultSet.getString("subject");
            grade = resultSet.getInt("grade");
            dekanatID = resultSet.getInt("dekanatID");

            list.add(new GradeEntity(subject, grade, dekanatID));
        }

        connection.close();
        statement.close();
        resultSet.close();

        return list;
    }
}
