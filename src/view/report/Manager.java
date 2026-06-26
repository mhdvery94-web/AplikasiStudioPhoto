package view.report;

import controller.ManagerController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.print.PrinterException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.time.LocalDate;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import model.User;
import view.Login;

public class Manager extends JFrame {

    private final ManagerController controller;
    private final User currentUser;

    private JTable reportTable;
    private DefaultTableModel reportTableModel;
    private JComboBox<String> cmbFilter;
    private JLabel lblTransaksi, lblPelanggan, lblOrder, lblOmzet;
    private JLabel lblBulanTransaksi, lblBulanPelanggan, lblBulanOrder, lblBulanOmzet;

    public Manager(User user) {
        this.currentUser = user;
        this.controller = new ManagerController();
        initUI();
    }

    private void initUI() {
        setTitle("Studio Photo - Dashboard Kepala Toko | " + currentUser.getNama());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 750);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(buildHeader(), BorderLayout.NORTH);
        mainPanel.add(buildContent(), BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private JPanel buildHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(76, 175, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("STUDIO PHOTO - DASHBOARD KEPALA TOKO");
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

    private JPanel buildContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        filterPanel.add(new JLabel("Filter:"));
        cmbFilter = new JComboBox<>(new String[]{"Hari Ini", "Bulan Ini", "Semua"});
        cmbFilter.addActionListener(e -> refreshReport());
        filterPanel.add(cmbFilter);

        JButton btnPrint = new JButton("Cetak Laporan");
        btnPrint.addActionListener(e -> printReport());
        filterPanel.add(btnPrint);

        panel.add(filterPanel);

        // Summary cards - Hari Ini
        JPanel cardsToday = new JPanel(new GridLayout(1, 4, 15, 0));
        cardsToday.setMaximumSize(new Dimension(1000, 110));
        cardsToday.setBorder(BorderFactory.createTitledBorder("Ringkasan Hari Ini"));

        cardsToday.add(createCard("Total Transaksi", "#2196F3", lblTransaksi = new JLabel()));
        cardsToday.add(createCard("Total Pelanggan", "#4CAF50", lblPelanggan = new JLabel()));
        cardsToday.add(createCard("Total Order", "#FF9800", lblOrder = new JLabel()));
        cardsToday.add(createCard("Total Omzet", "#9C27B0", lblOmzet = new JLabel()));

        // Summary cards - Bulan Ini
        JPanel cardsMonth = new JPanel(new GridLayout(1, 4, 15, 0));
        cardsMonth.setMaximumSize(new Dimension(1000, 110));
        cardsMonth.setBorder(BorderFactory.createTitledBorder("Ringkasan Bulan Ini"));

        cardsMonth.add(createCard("Total Transaksi", "#009688", lblBulanTransaksi = new JLabel()));
        cardsMonth.add(createCard("Total Pelanggan", "#8BC34A", lblBulanPelanggan = new JLabel()));
        cardsMonth.add(createCard("Total Order", "#FF5722", lblBulanOrder = new JLabel()));
        cardsMonth.add(createCard("Total Omzet", "#673AB7", lblBulanOmzet = new JLabel()));

        // Table
        String[] cols = {"No", "No. Order", "Pelanggan", "Ukuran", "Jumlah", "Total", "Waktu"};
        reportTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        reportTable = new JTable(reportTableModel);
        reportTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        reportTable.getColumnModel().getColumn(3).setPreferredWidth(60);
        reportTable.getColumnModel().getColumn(4).setPreferredWidth(60);

        JScrollPane scroll = new JScrollPane(reportTable);

        panel.add(cardsToday);
        panel.add(cardsMonth);
        panel.add(scroll);

        refreshReport();
        return panel;
    }

    private JPanel createCard(String title, String colorHex, JLabel valueLabel) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.decode(colorHex));
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTitle.setAlignmentX(CENTER_ALIGNMENT);

        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setAlignmentX(CENTER_ALIGNMENT);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(lblTitle);
        card.add(valueLabel);
        return card;
    }

    private void refreshReport() {
        DecimalFormat df = new DecimalFormat("#,###");
        String filter = (String) cmbFilter.getSelectedItem();
        LocalDate now = LocalDate.now();

        // Update daily cards
        double[] daily = controller.getRingkasanHarian();
        lblTransaksi.setText("<html><center><b>" + (int) daily[0] + "</b></center></html>");
        lblPelanggan.setText("<html><center><b>" + (int) daily[1] + "</b></center></html>");
        lblOrder.setText("<html><center><b>" + (int) daily[2] + "</b></center></html>");
        lblOmzet.setText("<html><center><b>Rp " + df.format(daily[3]) + "</b></center></html>");

        // Update monthly cards
        double[] monthly = controller.getRingkasanBulanIni();
        lblBulanTransaksi.setText("<html><center><b>" + (int) monthly[0] + "</b></center></html>");
        lblBulanPelanggan.setText("<html><center><b>" + (int) monthly[1] + "</b></center></html>");
        lblBulanOrder.setText("<html><center><b>" + (int) monthly[2] + "</b></center></html>");
        lblBulanOmzet.setText("<html><center><b>Rp " + df.format(monthly[3]) + "</b></center></html>");

        // Update table
        reportTableModel.setRowCount(0);
        if ("Hari Ini".equals(filter)) {
            controller.loadOrders(reportTableModel, now.toString(), now.toString());
        } else if ("Bulan Ini".equals(filter)) {
            controller.loadOrders(reportTableModel, now.withDayOfMonth(1).toString(), now.toString());
        } else {
            controller.loadAllOrders(reportTableModel);
        }
    }

    private void printReport() {
        try {
            MessageFormat header = new MessageFormat("LAPORAN STUDIO PHOTO - " + cmbFilter.getSelectedItem());
            MessageFormat footer = new MessageFormat("Halaman {0}");
            reportTable.print(JTable.PrintMode.FIT_WIDTH, header, footer);
            JOptionPane.showMessageDialog(this, "Laporan berhasil dicetak!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(this, "Gagal mencetak: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            new Login().setVisible(true);
            this.dispose();
        }
    }
}
