package by.bsuir.server.nazarchuk.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DataBaseConnector {

    public static Connection getConnection(String url, String login, String pass)
            throws SQLException {
        Properties property = new Properties();
        property.put("user", login);
        property.put("password", pass);
        property.put("autoReconnect", "true");
        property.setProperty("characterEncoding","utf-8");
        return DriverManager.getConnection(url, property);
    }
    
}

