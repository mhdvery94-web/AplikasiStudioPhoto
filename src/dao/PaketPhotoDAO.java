package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import model.PaketPhoto;

public class PaketPhotoDAO {

    public List<PaketPhoto> getAll() {
        List<PaketPhoto> list = new ArrayList<>();
        String sql = "SELECT * FROM paket_photo ORDER BY nama_paket, ukuran";
        try (var conn = DatabaseConnection.getConnection();
             var ps = conn.prepareStatement(sql);
             var rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new PaketPhoto(
                    rs.getInt("id"),
                    rs.getString("nama_paket"),
                    rs.getString("ukuran"),
                    rs.getDouble("harga"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    /** Nama paket unik (untuk ComboBox). */
    public String[] getDistinctNamaPaket() {
        Set<String> set = new LinkedHashSet<>();
        for (PaketPhoto p : getAll()) {
            set.add(p.getNamaPaket());
        }
        return set.toArray(new String[0]);
    }

    /** Ukuran unik (untuk ComboBox). */
    public String[] getDistinctUkuran() {
        Set<String> set = new LinkedHashSet<>();
        for (PaketPhoto p : getAll()) {
            set.add(p.getUkuran());
        }
        return set.toArray(new String[0]);
    }

    /** Cari harga berdasarkan nama paket + ukuran. */
    public double getHarga(String namaPaket, String ukuran) {
        String sql = "SELECT harga FROM paket_photo WHERE nama_paket = ? AND ukuran = ?";
        try (var conn = DatabaseConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, namaPaket);
            ps.setString(2, ukuran);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("harga");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public boolean insert(PaketPhoto p) {
        String sql = "INSERT INTO paket_photo (nama_paket, ukuran, harga) VALUES (?, ?, ?)";
        try (var conn = DatabaseConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNamaPaket());
            ps.setString(2, p.getUkuran());
            ps.setDouble(3, p.getHarga());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean update(PaketPhoto p) {
        String sql = "UPDATE paket_photo SET nama_paket = ?, ukuran = ?, harga = ? WHERE id = ?";
        try (var conn = DatabaseConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNamaPaket());
            ps.setString(2, p.getUkuran());
            ps.setDouble(3, p.getHarga());
            ps.setInt(4, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM paket_photo WHERE id = ?";
        try (var conn = DatabaseConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}
