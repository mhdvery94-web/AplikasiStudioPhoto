package view.master;

import controller.AdminController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import model.User;
import view.Login;

public class Admin extends JFrame {

    private final AdminController controller;
    private final User currentUser;

    // User management
    private JTable userTable;
    private DefaultTableModel userTableModel;
    private JTextField txtUsername, txtNama;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRole;

    // Paket Photo
    private JTable paketTable;
    private DefaultTableModel paketTableModel;
    private JTextField txtNamaPaket, txtUkuranPaket, txtHargaPaket;

    // Diskon
    private JTable diskonTable;
    private DefaultTableModel diskonTableModel;
    private JTextField txtNamaDiskon, txtPersenDiskon;

    // Metode Bayar
    private JTable metodeTable;
    private DefaultTableModel metodeTableModel;
    private JTextField txtNamaMetode;

    // Order viewing
    private JTable orderTable;
    private DefaultTableModel orderTableModel;

    // Summary labels
    private JLabel lblTransaksi, lblPelanggan, lblOrder, lblOmzet, lblTotalUser;

    public Admin(User user) {
        this.currentUser = user;
        this.controller = new AdminController();
        initUI();
    }

    private void initUI() {
        setTitle("Studio Photo - Dashboard Admin | " + currentUser.getNama());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 700);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(buildHeader(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("User Management", buildUserPanel());
        tabs.addTab("Paket Photo", buildPaketPanel());
        tabs.addTab("Diskon", buildDiskonPanel());
        tabs.addTab("Metode Bayar", buildMetodePanel());
        tabs.addTab("Daftar Order", buildOrderPanel());
        tabs.addTab("Ringkasan", buildSummaryPanel());
        mainPanel.add(tabs, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    // ==================== HEADER ====================

    private JPanel buildHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(33, 150, 243));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("STUDIO PHOTO - ADMIN DASHBOARD");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        JLabel userLabel = new JLabel(currentUser.getNama() + " (" + currentUser.getRole() + ")");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rightPanel.add(userLabel);

        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> logout());
        rightPanel.add(btnLogout);

        panel.add(title, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);
        return panel;
    }

    // ==================== USER MANAGEMENT ====================

    private JPanel buildUserPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Form User"));
        formPanel.setPreferredSize(new Dimension(400, 200));

        formPanel.add(new JLabel("Username:"));
        txtUsername = new JTextField();
        formPanel.add(txtUsername);

        formPanel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        formPanel.add(txtPassword);

        formPanel.add(new JLabel("Nama Lengkap:"));
        txtNama = new JTextField();
        formPanel.add(txtNama);

        formPanel.add(new JLabel("Role:"));
        cmbRole = new JComboBox<>(new String[]{"Admin", "Kasir", "Kepala Toko"});
        formPanel.add(cmbRole);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnAdd = new JButton("Tambah");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Hapus");
        JButton btnClear = new JButton("Clear");

        btnAdd.addActionListener(e -> addUser());
        btnUpdate.addActionListener(e -> updateUser());
        btnDelete.addActionListener(e -> deleteUser());
        btnClear.addActionListener(e -> clearUserForm());

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);
        formPanel.add(btnPanel);
        formPanel.add(new JLabel());

        String[] cols = {"No", "ID", "Username", "Nama", "Role", "Created At"};
        userTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        userTable = new JTable(userTableModel);
        userTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        userTable.getColumnModel().getColumn(1).setPreferredWidth(40);
        userTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) selectUserRow();
        });

        controller.loadUsers(userTableModel);

        panel.add(formPanel, BorderLayout.WEST);
        panel.add(new JScrollPane(userTable), BorderLayout.CENTER);
        return panel;
    }

    private void selectUserRow() {
        int row = userTable.getSelectedRow();
        if (row >= 0) {
            txtUsername.setText((String) userTableModel.getValueAt(row, 2));
            txtPassword.setText("");
            txtNama.setText((String) userTableModel.getValueAt(row, 3));
            cmbRole.setSelectedItem(userTableModel.getValueAt(row, 4));
        }
    }

    private void addUser() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String nama = txtNama.getText().trim();
        String role = (String) cmbRole.getSelectedItem();

        if (username.isEmpty() || password.isEmpty() || nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (controller.insertUser(username, password, nama, role)) {
            JOptionPane.showMessageDialog(this, "User berhasil ditambahkan!");
            controller.loadUsers(userTableModel);
            clearUserForm();
            refreshSummary();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menambah user!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUser() {
        int row = userTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih user yang akan diupdate!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) userTableModel.getValueAt(row, 1);
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String nama = txtNama.getText().trim();
        String role = (String) cmbRole.getSelectedItem();

        if (controller.updateUser(id, username, password, nama, role)) {
            JOptionPane.showMessageDialog(this, "User berhasil diupdate!");
            controller.loadUsers(userTableModel);
            clearUserForm();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal update user!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUser() {
        int row = userTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih user yang akan dihapus!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) userTableModel.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus user ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deleteUser(id)) {
                JOptionPane.showMessageDialog(this, "User berhasil dihapus!");
                controller.loadUsers(userTableModel);
                clearUserForm();
                refreshSummary();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal hapus user!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearUserForm() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtNama.setText("");
        cmbRole.setSelectedIndex(0);
        userTable.clearSelection();
    }

    // ==================== PAKET PHOTO ====================

    private JPanel buildPaketPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Paket Photo"));
        formPanel.setPreferredSize(new Dimension(380, 180));

        formPanel.add(new JLabel("Nama Paket:"));
        txtNamaPaket = new JTextField();
        formPanel.add(txtNamaPaket);

        formPanel.add(new JLabel("Ukuran:"));
        txtUkuranPaket = new JTextField();
        formPanel.add(txtUkuranPaket);

        formPanel.add(new JLabel("Harga (Rp):"));
        txtHargaPaket = new JTextField();
        formPanel.add(txtHargaPaket);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        JButton btnAdd = new JButton("Tambah");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Hapus");
        JButton btnClear = new JButton("Clear");

        btnAdd.addActionListener(e -> addPaket());
        btnUpdate.addActionListener(e -> updatePaket());
        btnDelete.addActionListener(e -> deletePaket());
        btnClear.addActionListener(e -> clearPaketForm());

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);
        formPanel.add(btnPanel);
        formPanel.add(new JLabel());

        String[] cols = {"No", "ID", "Nama Paket", "Ukuran", "Harga"};
        paketTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        paketTable = new JTable(paketTableModel);
        paketTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        paketTable.getColumnModel().getColumn(1).setPreferredWidth(40);
        paketTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) selectPaketRow();
        });

        controller.loadPaket(paketTableModel);

        panel.add(formPanel, BorderLayout.WEST);
        panel.add(new JScrollPane(paketTable), BorderLayout.CENTER);
        return panel;
    }

    private void selectPaketRow() {
        int row = paketTable.getSelectedRow();
        if (row >= 0) {
            txtNamaPaket.setText((String) paketTableModel.getValueAt(row, 2));
            txtUkuranPaket.setText((String) paketTableModel.getValueAt(row, 3));
            txtHargaPaket.setText(String.valueOf(paketTableModel.getValueAt(row, 4)));
        }
    }

    private void addPaket() {
        String nama = txtNamaPaket.getText().trim();
        String ukuran = txtUkuranPaket.getText().trim();
        String hargaStr = txtHargaPaket.getText().trim();

        if (nama.isEmpty() || ukuran.isEmpty() || hargaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double harga = Double.parseDouble(hargaStr);
            if (controller.insertPaket(nama, ukuran, harga)) {
                JOptionPane.showMessageDialog(this, "Paket berhasil ditambahkan!");
                controller.loadPaket(paketTableModel);
                clearPaketForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambah paket! (kemungkinan duplikat nama+ukuran)", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Harga harus berupa angka!", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updatePaket() {
        int row = paketTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih paket yang akan diupdate!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) paketTableModel.getValueAt(row, 1);
        String nama = txtNamaPaket.getText().trim();
        String ukuran = txtUkuranPaket.getText().trim();
        String hargaStr = txtHargaPaket.getText().trim();

        try {
            double harga = Double.parseDouble(hargaStr);
            if (controller.updatePaket(id, nama, ukuran, harga)) {
                JOptionPane.showMessageDialog(this, "Paket berhasil diupdate!");
                controller.loadPaket(paketTableModel);
                clearPaketForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal update paket!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Harga harus berupa angka!", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deletePaket() {
        int row = paketTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih paket yang akan dihapus!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) paketTableModel.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus paket ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deletePaket(id)) {
                JOptionPane.showMessageDialog(this, "Paket berhasil dihapus!");
                controller.loadPaket(paketTableModel);
                clearPaketForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal hapus paket!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearPaketForm() {
        txtNamaPaket.setText("");
        txtUkuranPaket.setText("");
        txtHargaPaket.setText("");
        paketTable.clearSelection();
    }

    // ==================== DISKON ====================

    private JPanel buildDiskonPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Diskon"));
        formPanel.setPreferredSize(new Dimension(380, 150));

        formPanel.add(new JLabel("Nama Diskon:"));
        txtNamaDiskon = new JTextField();
        formPanel.add(txtNamaDiskon);

        formPanel.add(new JLabel("Persen (%):"));
        txtPersenDiskon = new JTextField();
        formPanel.add(txtPersenDiskon);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        JButton btnAdd = new JButton("Tambah");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Hapus");
        JButton btnClear = new JButton("Clear");

        btnAdd.addActionListener(e -> addDiskon());
        btnUpdate.addActionListener(e -> updateDiskon());
        btnDelete.addActionListener(e -> deleteDiskon());
        btnClear.addActionListener(e -> clearDiskonForm());

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);
        formPanel.add(btnPanel);
        formPanel.add(new JLabel());

        String[] cols = {"No", "ID", "Nama", "Persen"};
        diskonTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        diskonTable = new JTable(diskonTableModel);
        diskonTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        diskonTable.getColumnModel().getColumn(1).setPreferredWidth(40);
        diskonTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) selectDiskonRow();
        });

        controller.loadDiskon(diskonTableModel);

        panel.add(formPanel, BorderLayout.WEST);
        panel.add(new JScrollPane(diskonTable), BorderLayout.CENTER);
        return panel;
    }

    private void selectDiskonRow() {
        int row = diskonTable.getSelectedRow();
        if (row >= 0) {
            txtNamaDiskon.setText((String) diskonTableModel.getValueAt(row, 2));
            String persenStr = ((String) diskonTableModel.getValueAt(row, 3)).replace("%", "");
            txtPersenDiskon.setText(persenStr);
        }
    }

    private void addDiskon() {
        String nama = txtNamaDiskon.getText().trim();
        String persenStr = txtPersenDiskon.getText().trim();

        if (nama.isEmpty() || persenStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double persen = Double.parseDouble(persenStr);
            if (controller.insertDiskon(nama, persen)) {
                JOptionPane.showMessageDialog(this, "Diskon berhasil ditambahkan!");
                controller.loadDiskon(diskonTableModel);
                clearDiskonForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambah diskon! (kemungkinan duplikat nama)", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Persen harus berupa angka!", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateDiskon() {
        int row = diskonTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih diskon yang akan diupdate!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) diskonTableModel.getValueAt(row, 1);
        String nama = txtNamaDiskon.getText().trim();
        String persenStr = txtPersenDiskon.getText().trim();

        try {
            double persen = Double.parseDouble(persenStr);
            if (controller.updateDiskon(id, nama, persen)) {
                JOptionPane.showMessageDialog(this, "Diskon berhasil diupdate!");
                controller.loadDiskon(diskonTableModel);
                clearDiskonForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal update diskon!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Persen harus berupa angka!", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteDiskon() {
        int row = diskonTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih diskon yang akan dihapus!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) diskonTableModel.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus diskon ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deleteDiskon(id)) {
                JOptionPane.showMessageDialog(this, "Diskon berhasil dihapus!");
                controller.loadDiskon(diskonTableModel);
                clearDiskonForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal hapus diskon!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearDiskonForm() {
        txtNamaDiskon.setText("");
        txtPersenDiskon.setText("");
        diskonTable.clearSelection();
    }

    // ==================== METODE BAYAR ====================

    private JPanel buildMetodePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Metode Bayar"));
        formPanel.setPreferredSize(new Dimension(380, 120));

        formPanel.add(new JLabel("Nama Metode:"));
        txtNamaMetode = new JTextField();
        formPanel.add(txtNamaMetode);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        JButton btnAdd = new JButton("Tambah");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Hapus");
        JButton btnClear = new JButton("Clear");

        btnAdd.addActionListener(e -> addMetode());
        btnUpdate.addActionListener(e -> updateMetode());
        btnDelete.addActionListener(e -> deleteMetode());
        btnClear.addActionListener(e -> clearMetodeForm());

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);
        formPanel.add(btnPanel);
        formPanel.add(new JLabel());

        String[] cols = {"No", "ID", "Nama Metode"};
        metodeTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        metodeTable = new JTable(metodeTableModel);
        metodeTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        metodeTable.getColumnModel().getColumn(1).setPreferredWidth(40);
        metodeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) selectMetodeRow();
        });

        controller.loadMetodeBayar(metodeTableModel);

        panel.add(formPanel, BorderLayout.WEST);
        panel.add(new JScrollPane(metodeTable), BorderLayout.CENTER);
        return panel;
    }

    private void selectMetodeRow() {
        int row = metodeTable.getSelectedRow();
        if (row >= 0) {
            txtNamaMetode.setText((String) metodeTableModel.getValueAt(row, 2));
        }
    }

    private void addMetode() {
        String nama = txtNamaMetode.getText().trim();
        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama metode harus diisi!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (controller.insertMetodeBayar(nama)) {
            JOptionPane.showMessageDialog(this, "Metode bayar berhasil ditambahkan!");
            controller.loadMetodeBayar(metodeTableModel);
            clearMetodeForm();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menambah metode! (kemungkinan duplikat)", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateMetode() {
        int row = metodeTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih metode yang akan diupdate!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) metodeTableModel.getValueAt(row, 1);
        String nama = txtNamaMetode.getText().trim();

        if (controller.updateMetodeBayar(id, nama)) {
            JOptionPane.showMessageDialog(this, "Metode bayar berhasil diupdate!");
            controller.loadMetodeBayar(metodeTableModel);
            clearMetodeForm();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal update metode!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteMetode() {
        int row = metodeTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih metode yang akan dihapus!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) metodeTableModel.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus metode ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deleteMetodeBayar(id)) {
                JOptionPane.showMessageDialog(this, "Metode bayar berhasil dihapus!");
                controller.loadMetodeBayar(metodeTableModel);
                clearMetodeForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal hapus metode!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearMetodeForm() {
        txtNamaMetode.setText("");
        metodeTable.clearSelection();
    }

    // ==================== ORDER PANEL ====================

    private JPanel buildOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("DAFTAR SEMUA ORDER");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(title, BorderLayout.NORTH);

        String[] cols = {"No", "No. Order", "Pelanggan", "Ukuran", "Jumlah", "Total", "Waktu"};
        orderTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        orderTable = new JTable(orderTableModel);
        orderTable.getColumnModel().getColumn(0).setPreferredWidth(40);

        controller.loadAllOrders(orderTableModel);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> controller.loadAllOrders(orderTableModel));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.add(btnRefresh);

        panel.add(btnPanel, BorderLayout.SOUTH);
        panel.add(new JScrollPane(orderTable), BorderLayout.CENTER);
        return panel;
    }

    // ==================== SUMMARY PANEL ====================

    private JPanel buildSummaryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("RINGKASAN SISTEM");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setAlignmentX(CENTER_ALIGNMENT);

        JPanel cards = new JPanel(new GridLayout(1, 5, 20, 0));
        cards.setMaximumSize(new Dimension(900, 120));
        cards.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        cards.add(createCard("Total Transaksi", "#2196F3", lblTransaksi = new JLabel()));
        cards.add(createCard("Total Pelanggan", "#4CAF50", lblPelanggan = new JLabel()));
        cards.add(createCard("Total Order", "#FF9800", lblOrder = new JLabel()));
        cards.add(createCard("Total Omzet", "#9C27B0", lblOmzet = new JLabel()));
        cards.add(createCard("Total User", "#F44336", lblTotalUser = new JLabel()));

        JButton btnRefresh = new JButton("Refresh Ringkasan");
        btnRefresh.setAlignmentX(CENTER_ALIGNMENT);
        btnRefresh.addActionListener(e -> refreshSummary());

        panel.add(title);
        panel.add(cards);
        panel.add(btnRefresh);
        refreshSummary();
        return panel;
    }

    private JPanel createCard(String title, String colorHex, JLabel valueLabel) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.decode(colorHex));
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitle.setAlignmentX(CENTER_ALIGNMENT);

        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setAlignmentX(CENTER_ALIGNMENT);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(lblTitle);
        card.add(valueLabel);
        return card;
    }

    private void refreshSummary() {
        double[] r = controller.getRingkasanHarian();
        DecimalFormat df = new DecimalFormat("#,###");
        lblTransaksi.setText("<html><center><b>" + (int) r[0] + "</b></center></html>");
        lblPelanggan.setText("<html><center><b>" + (int) r[1] + "</b></center></html>");
        lblOrder.setText("<html><center><b>" + (int) r[2] + "</b></center></html>");
        lblOmzet.setText("<html><center><b>Rp " + df.format(r[3]) + "</b></center></html>");
        lblTotalUser.setText("<html><center><b>" + controller.getUserCount() + "</b></center></html>");
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            new Login().setVisible(true);
            this.dispose();
        }
    }
}
