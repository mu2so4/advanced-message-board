package ru.nsu.ccfit.announces;
import ru.nsu.ccfit.announces.db.AnnounceDB;
import ru.nsu.ccfit.announces.db.AnnounceQueries;
import ru.nsu.ccfit.announces.db.auth.AuthQueries;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();
        int count = scanner.nextInt();
        Integer[] ids = new Integer[count];
        for(int index = 0; index < count; index++) {
            ids[index] = scanner.nextInt();
        }
        scanner.close();
        try (AnnounceDB db = AnnounceDB.getInstance()) {
            AnnounceQueries.submitAnnounceSuggestion(text, ids);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
