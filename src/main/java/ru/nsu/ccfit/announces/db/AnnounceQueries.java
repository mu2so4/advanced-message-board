package ru.nsu.ccfit.announces.db;

import ru.nsu.ccfit.announces.db.auth.AuthQueries;
import ru.nsu.ccfit.announces.db.auth.AuthenticationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class AnnounceQueries {
    private AnnounceQueries() {}

    public static List<Announce> getAnnouncesById(int subjectId) throws SQLException {
        String query =
                """
                WITH
                    "AnnounceIds" AS (
                        SELECT
                            "Announces".announce_id,
                            text,
                            publish_time,
                            last_update_time,
                            create_time
                        FROM "AnnounceSubjects"
                            INNER JOIN "Announces"
                            ON "AnnounceSubjects".announce_id = "Announces".announce_id
                        WHERE
                            subject_id = ? AND
                            publish_time IS NOT NULL
                    )
                SELECT DISTINCT
                    "AnnounceIds".announce_id,
                    "AnnounceIds".text,
                    "AnnounceIds".publish_time,
                    "AnnounceIds".last_update_time,
                    "AnnounceIds".create_time,
                    ARRAY_AGG(DISTINCT subject_id) AS subject_ids,
                    ARRAY_AGG(DISTINCT media_id) AS media_ids
                FROM "AnnounceIds"
                    LEFT OUTER JOIN "AnnounceSubjects"
                        ON "AnnounceIds".announce_id = "AnnounceSubjects".announce_id
                    LEFT OUTER JOIN "Media"
                        ON "AnnounceIds".announce_id = "Media".announce_id
                GROUP BY
                    "AnnounceIds".announce_id,
                    "AnnounceIds".text,
                    "AnnounceIds".publish_time,
                    "AnnounceIds".last_update_time,
                    "AnnounceIds".create_time
                ORDER BY
                    publish_time DESC;
                """;
        PreparedStatement statement = AnnounceDB.getInstance().getConnection().
                prepareStatement(query);
        statement.setInt(1, subjectId);
        ResultSet set = statement.executeQuery();
        ArrayList<Announce> announces = new ArrayList<>();
        while(set.next()) {
            Announce announce = new Announce();
            announce.id = set.getInt("announce_id");
            announce.publishDate = set.getDate("publish_time");
            announce.lastEditDate = set.getDate("last_update_time");
            announce.creationDate = set.getDate("create_time");
            announce.text = set.getString("text");
            announce.subjectsIds = (Integer[]) set.getArray("subject_ids").getArray();
            announce.mediaIds = (Integer[]) set.getArray("media_ids").getArray();
            announces.add(announce);
        }
        return announces;
    }

    public void deleteAnnounce(String authToken, int announceId) throws SQLException,
            AuthenticationException {
        AuthQueries.checkToken(authToken);
        Connection connection = AnnounceDB.getInstance().getConnection();
        PreparedStatement statement = connection.
                prepareStatement("DELETE FROM \"Announces\" WHERE announce_id = ? ;");
        statement.setInt(1, announceId);
        statement.executeUpdate();
    }

    public static void publishAnnounce(String authToken, int announceId) throws SQLException,
            AuthenticationException {
        AuthQueries.checkToken(authToken);
        String query =
                """
                UPDATE "Announces"
                SET
                    publish_time = NOW()
                WHERE announce_id = ?;
                """;
        Connection connection = AnnounceDB.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, announceId);
        statement.executeUpdate();
    }

    public static ResultSet getSuggestions(String authToken) throws SQLException,
            AuthenticationException {
        AuthQueries.checkToken(authToken);
        String query =
                """
                SELECT DISTINCT
                    "Announces".announce_id,
                    text,
                    create_time
                FROM "Announces"
                    LEFT OUTER JOIN "Media"
                        ON "Announces".announce_id = "Media".announce_id
                WHERE
                    publish_time IS NULL
                ORDER BY
                    create_time DESC;
                """;
        Connection connection = AnnounceDB.getInstance().getConnection();
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
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

    public static void EditAnnounce(String authToken, int announceId, String newText)
            throws SQLException, AuthenticationException {
        AuthQueries.checkToken(authToken);
        String query =
                """
                UPDATE "Announces"
                SET
                    text = ?
                WHERE announce_id = ?;
                """;
        Connection connection = AnnounceDB.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, newText);
        statement.setInt(2, announceId);
        statement.executeUpdate();
    }
}
