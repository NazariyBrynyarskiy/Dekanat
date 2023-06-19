package studentdb.dbaccess;

abstract public class Connection {
    private final String CONNECTION;

    protected Connection() {
        CONNECTION = "jdbc:mysql://localhost:3306/Dekan";
    }

    protected String getCONNECTION() { return CONNECTION; }
}
