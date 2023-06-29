package studentservlets;

import com.nimbusds.jose.JOSEException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import studentdb.dbaccess.GradesDBAccess;
import studentdb.dbaccess.interfaces.SelectFromDB;
import studentdb.dbentities.GradeEntity;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;


@WebServlet(name = "UserGradesController", value = "/user-grades-controller")
public class GradesController extends HttpServlet {
    public GradesController() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, List<Integer>> getGrades(HttpServletRequest request) throws ParseException, JOSEException {
        CookiesController controller = new CookiesController();
        SelectFromDB<List<GradeEntity>> gradesDBAccess = new GradesDBAccess();
        List<GradeEntity> gradeList = new ArrayList<>(gradesDBAccess.select(controller.getToken(request)));

        Map<String, List<Integer>> subjectGrade = new HashMap<>();

        for (GradeEntity entity : gradeList) {
            String subjectName = entity.subjectName();
            int grade = entity.grade();

            if (subjectGrade.containsKey(subjectName)) {
                List<Integer> gradesList = subjectGrade.get(subjectName);
                gradesList.add(grade);
            } else {
                List<Integer> gradesList = new ArrayList<>();
                gradesList.add(grade);
                subjectGrade.put(subjectName, gradesList);
            }
        }

            return subjectGrade;
    }

}
