package lecturerservlets;

import jakarta.servlet.http.HttpServletRequest;
import lecturerdb.dbaccess.StudentDBAccess;
import lecturerdb.dbenteties.StudentEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentsController {

    public StudentsController() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, List<StudentEntity>> getStudentEntities(HttpServletRequest request) {
        CookiesController controller = new CookiesController();
        StudentDBAccess studentDBAccess = new StudentDBAccess();

        Map<String, List<StudentEntity>> sortedListOfStudents = new HashMap<>();
        try {
            List<StudentEntity> listOfAllStudents = new ArrayList<>(
                    studentDBAccess.select(controller.getToken(request)));
            for (StudentEntity entity : listOfAllStudents) {
                if (sortedListOfStudents.containsKey(entity.groupName())) {
                    sortedListOfStudents.get(entity.groupName()).add(entity);
                } else {
                    List<StudentEntity> students = new ArrayList<>();
                    students.add(entity);
                    sortedListOfStudents.put(entity.groupName(), students);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return sortedListOfStudents;
    }
}
