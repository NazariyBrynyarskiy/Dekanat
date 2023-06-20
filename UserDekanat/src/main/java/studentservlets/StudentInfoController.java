package studentservlets;

import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import studentdb.dbaccess.FacultyDBAccess;
import studentdb.dbaccess.interfaces.SelectFromDB;

import java.text.ParseException;


public class StudentInfoController extends HttpServlet {

    public StudentInfoController() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String getFaculty(HttpServletRequest request) {
        CookiesController controller = new CookiesController();
        SelectFromDB<String> facultyDBAccess = new FacultyDBAccess();
        if (controller.getToken(request) != null) {
            try {
                return facultyDBAccess.select(controller.getToken(request));
            } catch (ParseException | JOSEException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
