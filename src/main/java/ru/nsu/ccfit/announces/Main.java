package ru.nsu.ccfit.announces;
import ru.nsu.ccfit.announces.db.AnnounceDB;
import ru.nsu.ccfit.announces.db.SqlModifiers;
import ru.nsu.ccfit.announces.db.SqlQueries;
import ru.nsu.ccfit.announces.subject.Subject;

import java.sql.ResultSet;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        scanner.close();
        try (AnnounceDB db = AnnounceDB.getInstance()) {
            SqlModifiers.addSubject(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
