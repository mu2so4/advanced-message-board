package ru.nsu.ccfit.announces;
import ru.nsu.ccfit.announces.db.Announce;
import ru.nsu.ccfit.announces.db.AnnounceDB;
import ru.nsu.ccfit.announces.db.AnnounceQueries;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        /*String login = scanner.nextLine();
        String password = scanner.nextLine();*/
        int id = scanner.nextInt();
        /*Integer[] ids = new Integer[scanner.nextInt()];
        for(int index = 0; index < ids.length; index++) {
            ids[index] = scanner.nextInt();
        }*/
        //int id = scanner.nextInt(); scanner.nextLine();
        //String newName = scanner.nextLine();
        scanner.close();
        try (AnnounceDB db = AnnounceDB.getInstance()) {
            //String token = AuthQueries.checkCredentials(login, password);
            List<Announce> announceList = AnnounceQueries.getAnnouncesById(id);
            System.out.println(announceList.size());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
