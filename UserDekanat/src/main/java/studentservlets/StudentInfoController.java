package studentservlets;

import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import studentdb.dbaccess.*;
import studentdb.dbaccess.interfaces.SelectFromDB;
import studentdb.dbentities.StudentEntity;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;


public class StudentInfoController extends HttpServlet {

    public StudentInfoController() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, String> getInfo(HttpServletRequest request) {
        CookiesController cookiesController = new CookiesController();

        SelectFromDB<StudentEntity> studentDBAccess = new StudentDBAccess();
        SelectFromDB<String> facultyDBAccess = new FacultyDBAccess();
        SelectFromDB<String> specialityDBAccess = new SpecialityDBAccess();
        SelectFromDB<String> groupDBAccess = new GroupDBAccess();
        SelectFromDB<String> formOfEducationDBAccess = new FormOfEducationDBAccess();

        Map<String, String> info = new HashMap<>();
        try {
            info.put("Surname", studentDBAccess.select(cookiesController.getToken(request)).surname());
            info.put("Name", studentDBAccess.select(cookiesController.getToken(request)).name());
            info.put("Faculty", facultyDBAccess.select(cookiesController.getToken(request)));
            info.put("Speciality", specialityDBAccess.select(cookiesController.getToken(request)));
            info.put("Group", groupDBAccess.select(cookiesController.getToken(request)));
            info.put("FormOfEducation", formOfEducationDBAccess.select(cookiesController.getToken(request)));
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }

        return info;
    }

}
