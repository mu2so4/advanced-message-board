package ru.nsu.ccfit.announces;
import ru.nsu.ccfit.announces.db.AnnounceDB;
import ru.nsu.ccfit.announces.db.AnnounceQueries;
import ru.nsu.ccfit.announces.db.SubjectQueries;
import ru.nsu.ccfit.announces.db.auth.AuthQueries;

import java.sql.ResultSet;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String login = scanner.nextLine();
        String password = scanner.nextLine();
        int id = scanner.nextInt(); scanner.nextLine();
        String newName = scanner.nextLine();
        scanner.close();
        try (AnnounceDB db = AnnounceDB.getInstance()) {
            String token = AuthQueries.checkCredentials(login, password);
            SubjectQueries.renameSubject(token, id, newName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
