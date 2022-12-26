package ru.nsu.ccfit.announces.db;

import ru.nsu.ccfit.announces.db.auth.AuthQueries;
import ru.nsu.ccfit.announces.db.auth.AuthenticationException;
import ru.nsu.ccfit.announces.subject.Subject;
import ru.nsu.ccfit.announces.subject.SubjectReader;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class SubjectQueries {
    private SubjectQueries() {}

    public static List<Subject> getAllSubjects() throws SQLException {
        Statement statement = AnnounceDB.getInstance().getConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM \"Subjects\" ORDER BY \"subject_id\";");
        ArrayList<Subject> subjects = new ArrayList<>();
        Subject subject;
        while((subject = SubjectReader.read(result)) != null) {
            subjects.add(subject);
        }
        return subjects;
    }

    public static Subject getSubjectById(int id) throws SQLException {
        PreparedStatement statement = AnnounceDB.getInstance().getConnection().
                prepareStatement("SELECT * FROM \"Subjects\" WHERE \"subject_id\"= ? ;");
        statement.setInt(1, id);
        ResultSet set = statement.executeQuery();
        if (set.next()) {
            return SubjectReader.read(set);
        } else {
            return null;
        }
    }

    public static void addSubjects(String authToken, String[] names) throws SQLException,
            AuthenticationException {
        AuthQueries.checkToken(authToken);
        Connection connection = AnnounceDB.getInstance().getConnection();
        PreparedStatement statement = connection.
                prepareStatement("INSERT INTO \"Subjects\"(\"subject_name\")" +
                        "VALUES (UNNEST(?));");
        Array arr = connection.createArrayOf("varchar", names);
        statement.setArray(1, arr);
        statement.executeUpdate();
    }

    public static void renameSubject(String authToken, int subjectId, String newName) throws SQLException,
            AuthenticationException {
        AuthQueries.checkToken(authToken);
        String query =
                """
                UPDATE "Subjects"
                SET subject_name = ?
                WHERE
                    subject_id = ?;
                """;
        PreparedStatement statement = AnnounceDB.getInstance().getConnection().
                prepareStatement(query);
        statement.setString(1, newName);
        statement.setInt(2, subjectId);
        statement.executeUpdate();
    }

    public static void removeSubjects(String authToken, Integer[] subjectIds)
            throws SQLException, AuthenticationException {
        AuthQueries.checkToken(authToken);
        String query =
                """
                DELETE FROM "Subjects"
                WHERE
                    subject_id = ANY(?);
                """;
        Connection connection = AnnounceDB.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        Array arr = connection.createArrayOf("integer", subjectIds);
        statement.setArray(1, arr);
        statement.executeUpdate();
    }
}
