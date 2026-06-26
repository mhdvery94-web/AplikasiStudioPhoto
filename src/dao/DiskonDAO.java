package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Diskon;

public class DiskonDAO {

    public List<Diskon> getAll() {
        List<Diskon> list = new ArrayList<>();
        String sql = "SELECT * FROM diskon ORDER BY persen";
        try (var conn = DatabaseConnection.getConnection();
             var ps = conn.prepareStatement(sql);
             var rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Diskon(
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getDouble("persen"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    /** Nama diskon untuk ComboBox (contoh: "Diskon 5%"). */
    public String[] getNamaList() {
        List<Diskon> all = getAll();
        String[] arr = new String[all.size()];
        for (int i = 0; i < all.size(); i++) {
            arr[i] = all.get(i).getNama();
        }
        return arr;
    }

    /** Cari persen berdasarkan nama. */
    public double getPersenByNama(String nama) {
        String sql = "SELECT persen FROM diskon WHERE nama = ?";
        try (var conn = DatabaseConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, nama);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("persen");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public boolean insert(Diskon d) {
        String sql = "INSERT INTO diskon (nama, persen) VALUES (?, ?)";
        try (var conn = DatabaseConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getNama());
            ps.setDouble(2, d.getPersen());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean update(Diskon d) {
        String sql = "UPDATE diskon SET nama = ?, persen = ? WHERE id = ?";
        try (var conn = DatabaseConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getNama());
            ps.setDouble(2, d.getPersen());
            ps.setInt(3, d.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM diskon WHERE id = ?";
        try (var conn = DatabaseConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}
