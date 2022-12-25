package ru.nsu.ccfit.announces.db;

import ru.nsu.ccfit.announces.subject.Subject;
import ru.nsu.ccfit.announces.subject.SubjectReader;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public final class SqlModifiers {
    private SqlModifiers() {}

    public static void addSubject(String name) throws SQLException {
        PreparedStatement statement = AnnounceDB.getInstance().getConnection().
                prepareStatement("INSERT INTO \"Subjects\"(\"subject_name\") VALUES (?);");
        statement.setString(1, name);
        statement.executeUpdate();
    }
}
