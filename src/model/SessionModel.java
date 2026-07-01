package model;

public class SessionModel {
    public static int idUser;
    public static String username;
    public static String nama;
    public static String role;

    public static void setSession(int id, String user, String namaUser, String userRole) {
        idUser = id;
        username = user;
        nama = namaUser;
        role = userRole;
    }

    public static void clearSession() {
        idUser = 0;
        username = null;
        nama = null;
        role = null;
    }
}