package studentdb.dbaccess.interfaces;

import com.nimbusds.jose.JOSEException;
import java.text.ParseException;

public interface SelectFromDB<T> {

    T select(String token) throws ParseException, JOSEException;

}
