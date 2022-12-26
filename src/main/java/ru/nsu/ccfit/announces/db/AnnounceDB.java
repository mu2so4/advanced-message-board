package ru.nsu.ccfit.announces.db;

import java.sql.*;

public class AnnounceDB implements AutoCloseable {
    private static AnnounceDB instance;
    private final Connection connection;

    private AnnounceDB() throws SQLException {
        connection = DriverManager.
                getConnection("jdbc:postgresql://localhost:5432/announces",
                        "announceDB", "12121212");
    }

    public static AnnounceDB getInstance() throws SQLException {
        if(instance == null) {
            instance = new AnnounceDB();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
