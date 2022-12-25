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

    public ResultSet sendQuery(String body, String[] args) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(body);
        for(int index = 0; index < args.length; index++) {
            statement.setString(index + 1, args[index]);
        }
        return statement.executeQuery();
    }

    public int insertOrUpdate(String body, String[] args) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(body);
        for(int index = 0; index < args.length; index++) {
            statement.setString(index + 1, args[index]);
        }
        return statement.executeUpdate();
    }

    public static AnnounceDB getInstance() throws SQLException {
        if(instance == null) {
            instance = new AnnounceDB();
        }
        return instance;
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
