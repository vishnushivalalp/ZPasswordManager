package sample;

import java.sql.Connection;
import java.sql.DriverManager;

public class homeDBConnection {
    public Connection databaselink;

    public Connection getConnection() {
        String databaseName = "zpasswordmanager";
        String databaseUsername = "root";
        String databasePassword = "root";
        String databaseUrl = "jdbc:mysql://localhost:3306/" + databaseName;

        try {
            databaselink = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return databaselink;
    }
}
