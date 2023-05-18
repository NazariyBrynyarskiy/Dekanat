package accountdb.access;

import java.sql.SQLException;

@FunctionalInterface
public interface Selector <T> {

    T select(String email, String password) throws SQLException;

}
