package db.studentinfo;

import db.abstracts.Connection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDBAccess extends Connection {
    public StudentDBAccess() {

    }

    public List<StudentEntity> select() throws SQLException {
        List<StudentEntity> list = new ArrayList<>();

        String name;
        String surname;
        String faculty;
        String studyingStart;
        String group;
        String formOfEducation;
        String trainingDirection;
        String formOfPayment;

        java.sql.Connection connection = DriverManager.getConnection(getCONNECTION(), "newuser", "password");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Students");

        while (resultSet.next()) {
            name = resultSet.getString("name");
            surname = resultSet.getString("surname");
            faculty = resultSet.getString("faculty");
            studyingStart = resultSet.getString("studyingStart");
            group = resultSet.getString("group");
            formOfEducation = resultSet.getString("formOfEducation");
            trainingDirection = resultSet.getString("trainingDirection");
            formOfPayment = resultSet.getString("formOfPayment");

            list.add(new StudentEntity(name,
                                       surname,
                                       faculty,
                                       studyingStart,
                                       group,
                                       formOfEducation,
                                       trainingDirection,
                                       formOfPayment
            ));
        }

        connection.close();
        statement.close();
        resultSet.close();

        return list;
    }

    public int getStudentID(int dekanatID) throws SQLException {
        java.sql.Connection connection = DriverManager.getConnection(getCONNECTION());
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT dekanatID FROM Students WHERE dekanatID = " + dekanatID);

        int studentID = Integer.parseInt(resultSet.getString("dekanatID"));

        connection.close();
        statement.close();
        resultSet.close();

        return studentID;
    }

}
