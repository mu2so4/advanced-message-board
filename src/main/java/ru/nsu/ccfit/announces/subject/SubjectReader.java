package ru.nsu.ccfit.announces.subject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SubjectReader {
    private SubjectReader() {}

    public static Subject read(ResultSet set) throws SQLException {
        if(!set.next()) {
            return null;
        }
        return new Subject(set.getInt("subject_id"),
            set.getString("subject_name"));
    }
}
