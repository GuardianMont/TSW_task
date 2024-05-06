package ec.model;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;


public class ConnectionPool {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/tavolando?serverTimezone=Europe/Rome";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "root";

    private static final DataSource dataSource;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find the driver in the classpath!", e);
        }

        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(JDBC_URL);
        ds.setUsername(JDBC_USER);
        ds.setPassword(JDBC_PASSWORD);
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
        dataSource = ds;
    }

    public static DataSource getInstance() {
        return dataSource;
    }
}