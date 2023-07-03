package lecturerservlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lecturerdb.dbaccess.GradesDBAccess;
import lecturerdb.dbenteties.GradeEntity;
import lecturerdb.dbenteties.StudentEntity;
import lecturerservlets.checker.CheckOneAttribute;
import lecturerservlets.checker.CheckTwoAttributes;
import lecturerservlets.checker.checkings.PositiveGrade;
import lecturerservlets.checker.checkings.RightFaculty;
import lecturerservlets.checker.checkings.RightSubject;
import lecturerservlets.checker.checkings.StudentExisting;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet(name = "GradesController", value = "/grades-controller")
public class GradesController extends HttpServlet {
    private final GradesDBAccess gradesDBAccess;

    public GradesController() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        gradesDBAccess = new GradesDBAccess();
    }

    public Map<String, List<GradeEntity>> getGradeEntities(HttpServletRequest request) {
        CookiesController controller = new CookiesController();
        GradesDBAccess gradesDBAccess = new GradesDBAccess();

        Map<String, List<GradeEntity>> sortedListOfGrades = new HashMap<>();
        try {
            List<GradeEntity> listOfAllGrades = new ArrayList<>(
                    gradesDBAccess.select(controller.getToken(request)));
            for (GradeEntity entity : listOfAllGrades) {
                if (sortedListOfGrades.containsKey(entity.subjectName())) {
                    sortedListOfGrades.get(entity.subjectName()).add(entity);
                } else {
                    List<GradeEntity> grades = new ArrayList<>();
                    grades.add(entity);
                    sortedListOfGrades.put(entity.subjectName(), grades);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return sortedListOfGrades;
    }

    private boolean checker(HttpServletRequest request, int grade, int dekanatID, String subjectName) {
        CookiesController cookiesController = new CookiesController();
        List<CheckOneAttribute> checksOneAttribute = new ArrayList<>();
        List<CheckTwoAttributes> checksTwoAttributes = new ArrayList<>();
        checksOneAttribute.add(new StudentExisting());
        checksOneAttribute.add(new PositiveGrade());
        checksTwoAttributes.add(new RightFaculty());
        checksTwoAttributes.add(new RightSubject());

        String token = cookiesController.getToken(request);
        if ( !checksOneAttribute.get(0).check(dekanatID) ||
                !checksOneAttribute.get(1).check(grade) ||
                !checksTwoAttributes.get(0).check(token, dekanatID) ||
                !checksTwoAttributes.get(1).check(token, subjectName)) {
            return false;
        }

        return true;
    }

    public void insert(HttpServletRequest request, String subjectName, int dekanatID, int grade) {
        if (checker(request, grade, dekanatID, subjectName)) {
            gradesDBAccess.insert(subjectName, dekanatID, grade);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String URL = "http://localhost:8080/admin/index";
        String subjectName;
        int dekanatID;
        int grade;

        if (request.getParameter("subjectName") != null &&
                request.getParameter("dekanatID") != null &&
                request.getParameter("grade") != null) {
            subjectName = request.getParameter("subjectName");
            dekanatID = Integer.parseInt(request.getParameter("dekanatID"));
            grade = Integer.parseInt(request.getParameter("grade"));
            insert(request, subjectName, dekanatID, grade);
            response.sendRedirect(URL);
        }
    }

}
