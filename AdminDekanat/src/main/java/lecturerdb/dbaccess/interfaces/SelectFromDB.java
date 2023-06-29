package lecturerdb.dbaccess.interfaces;

import com.nimbusds.jose.JOSEException;
import lecturerdb.dbenteties.GradeEntity;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public interface SelectFromDB<T> {

    T select(String token) throws SQLException, ParseException, JOSEException;

}
