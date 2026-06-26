package controller;

import dao.UserDAO;
import java.sql.SQLException;
import model.User;

public class LoginController {

    private final UserDAO userDAO;

    public LoginController() {
        this.userDAO = new UserDAO();
    }

    /**
     * Autentikasi user. Return User jika berhasil, null jika gagal.
     * Throws SQLException jika ada masalah koneksi database.
     */
    public User authenticate(String username, String password) throws SQLException {
        if (username.isEmpty() || password.isEmpty()) {
            return null;
        }
        return userDAO.login(username, password);
    }
}
