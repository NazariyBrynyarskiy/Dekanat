package studentdb.dbaccess.interfaces;

import com.nimbusds.jose.JOSEException;
import studentdb.dbentities.GradeEntity;

import java.text.ParseException;
import java.util.List;

public interface SelectFromDB<T> {

    T select(String token) throws ParseException, JOSEException;

}
