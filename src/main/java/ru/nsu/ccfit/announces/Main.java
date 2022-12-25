package ru.nsu.ccfit.announces;
import ru.nsu.ccfit.announces.db.AnnounceDB;
import ru.nsu.ccfit.announces.db.AuthQueries;
import ru.nsu.ccfit.announces.db.SqlModifiers;
import ru.nsu.ccfit.announces.db.SqlQueries;

import java.sql.ResultSet;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String login = scanner.nextLine();
        String password = scanner.nextLine();
        scanner.close();
        try (AnnounceDB db = AnnounceDB.getInstance()) {
            String id = AuthQueries.checkCredentials(login, password);
            System.out.println(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
