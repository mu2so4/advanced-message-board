package ru.nsu.ccfit.announces.db;

import ru.nsu.ccfit.announces.db.auth.AuthQueries;
import ru.nsu.ccfit.announces.db.auth.AuthenticationException;

import java.sql.*;

public final class AnnounceQueries {
    private AnnounceQueries() {}

    public static void addSubject(String name) throws SQLException {
        PreparedStatement statement = AnnounceDB.getInstance().getConnection().
                prepareStatement("INSERT INTO \"Subjects\"(\"subject_name\") VALUES (?);");
        statement.setString(1, name);
        statement.executeUpdate();
    }


    public static void submitAnnounceSuggestion(String text, Integer[] subjectIds) throws SQLException {
        String announceQuery =
                """
                INSERT
                INTO "Announces" (
                    text,
                    create_time
                )
                VALUES (
                    ?,
                    NOW()
                )
                RETURNING announce_id;
                """;
        String announceSubjectsQuery =
                """
                INSERT
                INTO "AnnounceSubjects" (announce_id, subject_id)
                VALUES(?, UNNEST(?));
                """;
        Connection connection = AnnounceDB.getInstance().getConnection();
        connection.setAutoCommit(false);
        PreparedStatement announceStatement = connection.prepareStatement(announceQuery);
        announceStatement.setString(1, text);
        ResultSet resultId = announceStatement.executeQuery();
        resultId.next();
        int id = resultId.getInt("announce_id");
        PreparedStatement subjectsStatement = connection.prepareStatement(announceSubjectsQuery);
        Array array = connection.createArrayOf("integer", subjectIds);
        subjectsStatement.setInt(1, id);
        subjectsStatement.setArray(2, array);
        subjectsStatement.executeUpdate();
        connection.commit();
        connection.setAutoCommit(true);
    }

}
