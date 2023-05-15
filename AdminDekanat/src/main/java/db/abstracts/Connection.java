package db.abstracts;

abstract public class Connection {
    private final String CONNECTION;

    public Connection() {
        CONNECTION = "jdbc:mysql://localhost:3306/Dekanat";
    }

    public String getCONNECTION() { return CONNECTION; }
}
