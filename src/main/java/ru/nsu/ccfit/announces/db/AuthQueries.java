package ru.nsu.ccfit.announces.db;

import java.security.SecureRandom;
import java.sql.*;
import java.util.Base64;

public class AuthQueries {
    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
    public static String checkCredentials(String login, String password) throws SQLException {
        Connection connection = AnnounceDB.getInstance().getConnection();
        PreparedStatement statement = connection.
                prepareStatement("SELECT user_id FROM \"Users\" WHERE \"login\"= ? AND \"password\" = ? ;");
        statement.setString(1, login);
        statement.setString(2, password);
        ResultSet set = statement.executeQuery();
        if(!set.next()) {
            return null;
        }
        int userId = set.getInt("user_id");
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        String token = base64Encoder.encodeToString(randomBytes);

        PreparedStatement hashStatement = connection.
                prepareStatement("INSERT INTO \"AuthTokens\"(token, user_id, expiration_time)" +
                        "VALUES(?, ?, NOW() + INTERVAL '10 min');");
        hashStatement.setString(1, token);
        hashStatement.setInt(2, userId);
        hashStatement.executeUpdate();
        return token;
    }

    public static int checkToken(String token) throws SQLException {
        Connection connection = AnnounceDB.getInstance().getConnection();
        PreparedStatement statement = connection.
                prepareStatement("SELECT * FROM \"AuthTokens\" WHERE \"token\"= ? AND " +
                        "NOW() <= \"expiration_time\";");
        statement.setString(1, token);
        ResultSet set = statement.executeQuery();
        if(!set.next()) {
            return -1;
        }
        return set.getInt("user_id");
    }

    public static void clearOldTokens() throws SQLException {
        Connection connection = AnnounceDB.getInstance().getConnection();
        Statement statement = connection.createStatement();
        statement.executeQuery("DELETE FROM \"AuthTokens\" WHERE NOW() > \"expiration_time\";");
    }
}
