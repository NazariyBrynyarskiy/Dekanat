package dao;

import java.sql.*;

public class Role {

    public String getRole(String email, String password) throws SQLException {
        try (java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Dekan", "newuser", "password");
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT 'admin' as role FROM Lecturers WHERE email = ? AND password = ? UNION SELECT 'user' as role FROM Students WHERE email = ? AND password = ?")) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("role");
                }
            }
        }

        return null;
    }


}
