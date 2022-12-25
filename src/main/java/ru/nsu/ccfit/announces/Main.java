package ru.nsu.ccfit.announces;
import ru.nsu.ccfit.announces.db.AnnounceDB;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String[] params = new String[1];
        Scanner scanner = new Scanner(System.in);
        params[0] = scanner.next();
        scanner.close();
        try (AnnounceDB db = AnnounceDB.getInstance()) {
            db.insertOrUpdate("INSERT INTO \"Subjects\"(\"subject_name\")" +
                    "VALUES (?)", params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
