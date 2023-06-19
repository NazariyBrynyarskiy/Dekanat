package lecturerservlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lecturermanagers.GradesManager;

import java.io.IOException;
import java.sql.SQLException;


@WebServlet(name = "GradesController", value = "/grades-controller")
public class GradesController extends HttpServlet {

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
            try {
                GradesManager gradesManager = new GradesManager();
                gradesManager.insert(subjectName, dekanatID, grade);
                response.sendRedirect(URL);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
