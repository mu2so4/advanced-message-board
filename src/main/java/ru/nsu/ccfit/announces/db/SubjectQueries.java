package ru.nsu.ccfit.announces.db;

import ru.nsu.ccfit.announces.db.auth.AuthQueries;
import ru.nsu.ccfit.announces.db.auth.AuthenticationException;
import ru.nsu.ccfit.announces.subject.Subject;
import ru.nsu.ccfit.announces.subject.SubjectReader;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

}
