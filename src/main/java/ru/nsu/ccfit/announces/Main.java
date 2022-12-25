package ru.nsu.ccfit.announces;
import java.sql.*;
public class Main {
    public static void main(String[] args) {
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/announces",
                            "announceDB", "12121212");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(1);
        }
        System.out.println("Database opened successfully");
    }
}
