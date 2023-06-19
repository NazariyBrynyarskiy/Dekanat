package studentservlets;

import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import studentdb.dbaccess.GradesDBAccess;
import studentdb.dbentities.GradeEntity;

import java.text.ParseException;
import java.util.List;

public class GradesController extends HttpServlet {
    public GradesController() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<GradeEntity> getGrades(HttpServletRequest request) throws ParseException, JOSEException {
        CookiesController controller = new CookiesController();
        GradesDBAccess gradesDBAccess = new GradesDBAccess();
        if (controller.getToken(request) != null) {
            List<GradeEntity> grades = gradesDBAccess.select(controller.getToken(request));
            return grades;
        }
        return null;
    }
}
