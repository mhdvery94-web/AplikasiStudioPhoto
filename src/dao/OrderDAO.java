package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Order;

public class OrderDAO {

    /**
     * Generate nomor order otomatis: ORD-YYYYMMDD-XXX (counter global).
     */
    public String generateNoOrder() {
        String today = java.time.LocalDate.now().toString().replace("-", "");
        String prefix = "ORD-" + today + "-";
        String sql = "SELECT MAX(no) FROM orders";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String maxNo = rs.getString(1);
                if (maxNo != null) {
                    // Ambil 3 digit terakhir sebagai counter, increment
                    int lastDash = maxNo.lastIndexOf('-');
                    int counter = Integer.parseInt(maxNo.substring(lastDash + 1)) + 1;
                    return prefix + String.format("%03d", counter);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prefix + "001";
    }

    /**
     * Simpan order baru ke database.
     */
    public boolean insert(Order order) {
        String sql = "INSERT INTO orders (no, pelanggan, ukuran, jumlah, biaya) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, order.getNo());
            ps.setString(2, order.getPelanggan());
            ps.setString(3, order.getUkuran());
            ps.setInt(4, order.getJumlah());
            ps.setDouble(5, order.getBiaya());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Ambil semua order, urut dari terbaru.
     */
    public List<Order> getAll() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT id, no, pelanggan, ukuran, jumlah, biaya, created_at, updated_at "
                   + "FROM orders ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Order(
                    rs.getInt("id"),
                    rs.getString("no"),
                    rs.getString("pelanggan"),
                    rs.getString("ukuran"),
                    rs.getInt("jumlah"),
                    rs.getDouble("biaya"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Cari order berdasarkan nomor order atau nama pelanggan.
     */
    public List<Order> search(String keyword) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT id, no, pelanggan, ukuran, jumlah, biaya, created_at, updated_at "
                   + "FROM orders WHERE no LIKE ? OR pelanggan LIKE ? ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Order(
                        rs.getInt("id"),
                        rs.getString("no"),
                        rs.getString("pelanggan"),
                        rs.getString("ukuran"),
                        rs.getInt("jumlah"),
                        rs.getDouble("biaya"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Ambil ringkasan harian: total transaksi, total pelanggan, total order, total omzet.
     * Return array: [totalTransaksi, totalPelanggan, totalOrder, totalOmzet]
     */
    public double[] getRingkasanHarian() {
        String sql = "SELECT COUNT(*) AS total_transaksi, "
                   + "COUNT(DISTINCT pelanggan) AS total_pelanggan, "
                   + "SUM(jumlah) AS total_order, "
                   + "SUM(biaya) AS total_omzet "
                   + "FROM orders WHERE DATE(created_at) = CURDATE()";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return new double[] {
                    rs.getDouble("total_transaksi"),
                    rs.getDouble("total_pelanggan"),
                    rs.getDouble("total_order"),
                    rs.getDouble("total_omzet")
                };
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new double[] {0, 0, 0, 0};
    }

    /**
     * Ringkasan berdasarkan rentang tanggal.
     */
    public double[] getRingkasanByTanggal(String startDate, String endDate) {
        String sql = "SELECT COUNT(*) AS total_transaksi, "
                   + "COUNT(DISTINCT pelanggan) AS total_pelanggan, "
                   + "SUM(jumlah) AS total_order, "
                   + "SUM(biaya) AS total_omzet "
                   + "FROM orders WHERE DATE(created_at) BETWEEN ? AND ?";

        try (var conn = DatabaseConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new double[] {
                        rs.getDouble("total_transaksi"),
                        rs.getDouble("total_pelanggan"),
                        rs.getDouble("total_order"),
                        rs.getDouble("total_omzet")
                    };
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new double[] {0, 0, 0, 0};
    }

    /**
     * Ambil order berdasarkan rentang tanggal.
     */
    public List<Order> getByDateRange(String startDate, String endDate) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT id, no, pelanggan, ukuran, jumlah, biaya, created_at, updated_at "
                   + "FROM orders WHERE DATE(created_at) BETWEEN ? AND ? ORDER BY created_at DESC";

        try (var conn = DatabaseConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Order(
                        rs.getInt("id"),
                        rs.getString("no"),
                        rs.getString("pelanggan"),
                        rs.getString("ukuran"),
                        rs.getInt("jumlah"),
                        rs.getDouble("biaya"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
