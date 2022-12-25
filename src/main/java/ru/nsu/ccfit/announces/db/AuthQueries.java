package ru.nsu.ccfit.announces.db;

import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

public class AuthQueries {
    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
    public static String checkCredentials(String login, String password) throws SQLException {
        PreparedStatement statement = AnnounceDB.getInstance().getConnection().
                prepareStatement("SELECT user_id FROM \"Users\" WHERE \"login\"= ? AND \"password\" = ? ;");
        statement.setString(1, login);
        statement.setString(2, password);
        ResultSet set = statement.executeQuery();
        if(!set.next()) {
            return null;
        }
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
