package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.MetodeBayar;

public class MetodeBayarDAO {

    public List<MetodeBayar> getAll() {
        List<MetodeBayar> list = new ArrayList<>();
        String sql = "SELECT * FROM metode_bayar ORDER BY nama";
        try (var conn = DatabaseConnection.getConnection();
             var ps = conn.prepareStatement(sql);
             var rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new MetodeBayar(
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    /** Nama list untuk ComboBox. */
    public String[] getNamaList() {
        List<MetodeBayar> all = getAll();
        String[] arr = new String[all.size()];
        for (int i = 0; i < all.size(); i++) {
            arr[i] = all.get(i).getNama();
        }
        return arr;
    }

    public boolean insert(MetodeBayar m) {
        String sql = "INSERT INTO metode_bayar (nama) VALUES (?)";
        try (var conn = DatabaseConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getNama());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean update(MetodeBayar m) {
        String sql = "UPDATE metode_bayar SET nama = ? WHERE id = ?";
        try (var conn = DatabaseConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getNama());
            ps.setInt(2, m.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM metode_bayar WHERE id = ?";
        try (var conn = DatabaseConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}
