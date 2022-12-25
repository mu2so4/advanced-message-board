package ru.nsu.ccfit.announces;
import ru.nsu.ccfit.announces.db.AnnounceDB;
import ru.nsu.ccfit.announces.db.SqlQueries;
import ru.nsu.ccfit.announces.subject.Subject;

import java.sql.ResultSet;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        /*String[] params = new String[1];
        Scanner scanner = new Scanner(System.in);
        params[0] = scanner.nextLine();
        scanner.close();*/
        try (AnnounceDB db = AnnounceDB.getInstance()) {
            List<Subject> subjects = SqlQueries.getAllSubjects();
            for(Subject subject: subjects) {
                System.out.println(subject);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
