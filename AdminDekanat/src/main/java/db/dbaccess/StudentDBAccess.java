package db.dbaccess;

import db.dbenteties.StudentEntity;

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

        java.sql.Connection connection = DriverManager.getConnection(getCONNECTION(), "newuser", "password");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Students");

        while (resultSet.next()) {
            dekanatID = resultSet.getInt("dekanatID");
            name = resultSet.getString("name");
            surname = resultSet.getString("surname");
            groupName = resultSet.getString("groupName");
            formOfEducation = resultSet.getString("formOfEducation");
            formOfPayment = resultSet.getString("formOfPayment");

            list.add(new StudentEntity(dekanatID, name, surname, groupName, formOfEducation, formOfPayment));
        }

        connection.close();
        statement.close();
        resultSet.close();

        return list;
    }

    public int getStudentID(int dekanatID) throws SQLException {
        java.sql.Connection connection = DriverManager.getConnection(getCONNECTION(), "newuser", "password");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT dekanatID FROM Students WHERE dekanatID = " + dekanatID);

        int studentID = 0;
        if (resultSet.next()) {
            studentID = Integer.parseInt(resultSet.getString("dekanatID"));
        }

        connection.close();
        statement.close();
        resultSet.close();

        return studentID;
    }

}
