package view.master;

import dao.DatabaseConnection;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class KelolaPaketPhoto extends JInternalFrame {

    private JTextField txtId;
    private JTextField txtNamaPaket;
    private JTextField txtUkuran;
    private JTextField txtHarga;
    private JTextField txtCari;

    private JTable table;
    private DefaultTableModel model;

    private JButton btnSimpan;
    private JButton btnUpdate;
    private JButton btnHapus;
    private JButton btnReset;
    private JButton btnCari;
    private JButton btnRefresh;

    public KelolaPaketPhoto() {
        super("Kelola Paket Photo", true, true, true, true);
        setSize(610, 470);
        initManual();
        loadData("");
    }

    private void initManual() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelForm = new JPanel(new GridLayout(5, 2, 8, 8));

        txtId = new JTextField();
        txtId.setEditable(false);

        txtNamaPaket = new JTextField();
        txtUkuran = new JTextField();
        txtHarga = new JTextField();
        txtCari = new JTextField();

        panelForm.add(new JLabel("ID"));
        panelForm.add(txtId);

        panelForm.add(new JLabel("Nama Paket"));
        panelForm.add(txtNamaPaket);

        panelForm.add(new JLabel("Ukuran Photo"));
        panelForm.add(txtUkuran);

        panelForm.add(new JLabel("Harga"));
        panelForm.add(txtHarga);

        panelForm.add(new JLabel("Cari"));
        panelForm.add(txtCari);

        JPanel panelButton = new JPanel();

        btnSimpan = new JButton("Simpan");
        btnUpdate = new JButton("Update");
        btnHapus = new JButton("Hapus");
        btnReset = new JButton("Reset");
        btnCari = new JButton("Cari");
        btnRefresh = new JButton("Refresh");

        panelButton.add(btnSimpan);
        panelButton.add(btnUpdate);
        panelButton.add(btnHapus);
        panelButton.add(btnReset);
        panelButton.add(btnCari);
        panelButton.add(btnRefresh);

        JPanel panelAtas = new JPanel(new BorderLayout(5, 5));
        panelAtas.add(panelForm, BorderLayout.CENTER);
        panelAtas.add(panelButton, BorderLayout.SOUTH);

        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nama Paket");
        model.addColumn("Ukuran");
        model.addColumn("Harga");

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        add(panelAtas, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnSimpan.addActionListener(e -> simpanData());
        btnUpdate.addActionListener(e -> updateData());
        btnHapus.addActionListener(e -> hapusData());
        btnReset.addActionListener(e -> resetForm());

        btnCari.addActionListener(e -> {
            loadData(txtCari.getText());
        });

        btnRefresh.addActionListener(e -> {
            txtCari.setText("");
            loadData("");
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int row = table.getSelectedRow();

                txtId.setText(model.getValueAt(row, 0).toString());
                txtNamaPaket.setText(model.getValueAt(row, 1).toString());
                txtUkuran.setText(model.getValueAt(row, 2).toString());
                txtHarga.setText(model.getValueAt(row, 3).toString());
            }
        });
    }

    private void loadData(String keyword) {
        model.setRowCount(0);

        String sql = "SELECT id, nama_paket, ukuran, harga "
                + "FROM paket_photo "
                + "WHERE nama_paket LIKE ? OR ukuran LIKE ? "
                + "ORDER BY nama_paket, ukuran";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            String cari = "%" + keyword + "%";
            ps.setString(1, cari);
            ps.setString(2, cari);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nama_paket"),
                    rs.getString("ukuran"),
                    rs.getDouble("harga")
                });
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal load data paket photo: " + e.getMessage());
        }
    }

    private void simpanData() {
        String namaPaket = txtNamaPaket.getText().trim();
        String ukuran = txtUkuran.getText().trim();
        String hargaText = txtHarga.getText().trim();

        if (namaPaket.isEmpty() || ukuran.isEmpty() || hargaText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama paket, ukuran, dan harga wajib diisi.");
            return;
        }

        double harga;

        try {
            harga = parseHarga(hargaText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga harus berupa angka. Contoh: 15000 atau 150.000");
            return;
        }

        try {
            if (dataSudahAda(namaPaket, ukuran)) {
                JOptionPane.showMessageDialog(this, "Data paket dan ukuran ini sudah ada.");
                return;
            }

            String sql = "INSERT INTO paket_photo (nama_paket, ukuran, harga) VALUES (?, ?, ?)";

            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, namaPaket);
            ps.setString(2, ukuran);
            ps.setDouble(3, harga);

            ps.executeUpdate();
            ps.close();

            JOptionPane.showMessageDialog(this, "Data paket photo berhasil disimpan.");

            resetForm();
            loadData("");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal simpan data: " + e.getMessage());
        }
    }

    private void updateData() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diupdate dari tabel.");
            return;
        }

        String namaPaket = txtNamaPaket.getText().trim();
        String ukuran = txtUkuran.getText().trim();
        String hargaText = txtHarga.getText().trim();

        if (namaPaket.isEmpty() || ukuran.isEmpty() || hargaText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama paket, ukuran, dan harga wajib diisi.");
            return;
        }

        double harga;

        try {
            harga = parseHarga(hargaText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga harus berupa angka. Contoh: 15000 atau 150.000");
            return;
        }

        try {
            int id = Integer.parseInt(txtId.getText());

            if (dataSudahAdaSelainId(namaPaket, ukuran, id)) {
                JOptionPane.showMessageDialog(this, "Data paket dan ukuran ini sudah digunakan oleh data lain.");
                return;
            }

            String sql = "UPDATE paket_photo SET nama_paket = ?, ukuran = ?, harga = ? WHERE id = ?";

            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, namaPaket);
            ps.setString(2, ukuran);
            ps.setDouble(3, harga);
            ps.setInt(4, id);

            ps.executeUpdate();
            ps.close();

            JOptionPane.showMessageDialog(this, "Data paket photo berhasil diupdate.");

            resetForm();
            loadData("");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal update data: " + e.getMessage());
        }
    }

    private void hapusData() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus dari tabel.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Yakin ingin menghapus data ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            String sql = "DELETE FROM paket_photo WHERE id = ?";

            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, Integer.parseInt(txtId.getText()));
            ps.executeUpdate();
            ps.close();

            JOptionPane.showMessageDialog(this, "Data paket photo berhasil dihapus.");

            resetForm();
            loadData("");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal hapus data: " + e.getMessage());
        }
    }

    private boolean dataSudahAda(String namaPaket, String ukuran) throws SQLException {
        String sql = "SELECT id FROM paket_photo WHERE nama_paket = ? AND ukuran = ? LIMIT 1";

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, namaPaket);
        ps.setString(2, ukuran);

        ResultSet rs = ps.executeQuery();

        boolean ada = rs.next();

        rs.close();
        ps.close();

        return ada;
    }

    private boolean dataSudahAdaSelainId(String namaPaket, String ukuran, int id) throws SQLException {
        String sql = "SELECT id FROM paket_photo "
                + "WHERE nama_paket = ? AND ukuran = ? AND id <> ? LIMIT 1";

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, namaPaket);
        ps.setString(2, ukuran);
        ps.setInt(3, id);

        ResultSet rs = ps.executeQuery();

        boolean ada = rs.next();

        rs.close();
        ps.close();

        return ada;
    }

    private double parseHarga(String text) {
        String harga = text.replace("Rp", "")
                .replace("rp", "")
                .replace(" ", "")
                .trim();

        harga = harga.replace(".", "");

        return Double.parseDouble(harga);
    }

    private void resetForm() {
        txtId.setText("");
        txtNamaPaket.setText("");
        txtUkuran.setText("");
        txtHarga.setText("");
        txtCari.setText("");
        table.clearSelection();
    }
}