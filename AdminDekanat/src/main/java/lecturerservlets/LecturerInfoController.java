package lecturerservlets;

import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import lecturerdb.dbaccess.interfaces.Connection;
import lecturerdb.dbaccess.LecturerDBAccess;
import lecturerdb.dbaccess.interfaces.SelectFromDB;
import lecturerdb.dbenteties.LecturerEntity;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class LecturerInfoController {

    public Map<String, String> getInfo(HttpServletRequest request) {
        CookiesController cookiesController = new CookiesController();

        Connection connection = new LecturerDBAccess();
        SelectFromDB<LecturerEntity> lecturerDBAccessInfo = new LecturerDBAccess();

        Map<String, String> info = new HashMap<>();
        try {
            info.put("Faculty", connection.getLecturerFaculty(cookiesController.getToken(request)));
            info.put("Name", lecturerDBAccessInfo.select(cookiesController.getToken(request)).name());
            info.put("Surname", lecturerDBAccessInfo.select(cookiesController.getToken(request)).surname());
        } catch (SQLException | ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }

        return info;
    }
}
