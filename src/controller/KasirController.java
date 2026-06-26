package controller;

import dao.DiskonDAO;
import dao.MetodeBayarDAO;
import dao.OrderDAO;
import dao.PaketPhotoDAO;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import model.Order;

public class KasirController {

    private final OrderDAO orderDAO;
    private final PaketPhotoDAO paketDAO;
    private final DiskonDAO diskonDAO;
    private final MetodeBayarDAO metodeBayarDAO;

    public KasirController() {
        this.orderDAO = new OrderDAO();
        this.paketDAO = new PaketPhotoDAO();
        this.diskonDAO = new DiskonDAO();
        this.metodeBayarDAO = new MetodeBayarDAO();
    }

    // --- Data untuk ComboBox (dari DB) ---

    public String[] getPaketList() {
        return paketDAO.getDistinctNamaPaket();
    }

    public String[] getUkuranList() {
        return paketDAO.getDistinctUkuran();
    }

    public String[] getDiskonList() {
        return diskonDAO.getNamaList();
    }

    public String[] getMetodeBayarList() {
        return metodeBayarDAO.getNamaList();
    }

    // --- Bisnis Logic ---

    public String generateNoOrder() {
        return orderDAO.generateNoOrder();
    }

    /**
     * Harga satuan dari DB berdasarkan paket + ukuran.
     */
    public double getHargaSatuan(String paket, String ukuran) {
        return paketDAO.getHarga(paket, ukuran);
    }

    /**
     * Hitung diskon nominal. Ambil persen dari DB berdasarkan nama diskon.
     */
    public double getNominalDiskon(double total, String namaDiskon) {
        double persen = diskonDAO.getPersenByNama(namaDiskon);
        return total * (persen / 100.0);
    }

    /**
     * Hitung total bayar = (harga_satuan * jumlah) - diskon.
     */
    public double hitungTotal(double hargaSatuan, int jumlah, String namaDiskon) {
        double subtotal = hargaSatuan * jumlah;
        double diskon = getNominalDiskon(subtotal, namaDiskon);
        return subtotal - diskon;
    }

    /**
     * Simpan order.
     */
    public boolean simpanOrder(String no, String pelanggan, String ukuran,
                               int jumlah, double totalBayar) {
        Order order = new Order();
        order.setNo(no);
        order.setPelanggan(pelanggan);
        order.setUkuran(ukuran);
        order.setJumlah(jumlah);
        order.setBiaya(totalBayar);
        return orderDAO.insert(order);
    }

    // --- Data Table ---

    public void loadOrders(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        List<Order> list = orderDAO.getAll();
        int no = 1;
        for (Order o : list) {
            tableModel.addRow(new Object[] {
                no++,
                o.getNo(),
                o.getPelanggan(),
                "-",
                o.getUkuran(),
                o.getJumlah(),
                o.getBiaya(),
                o.getCreatedAt()
            });
        }
    }

    public void searchOrders(DefaultTableModel tableModel, String keyword) {
        tableModel.setRowCount(0);
        List<Order> list = orderDAO.search(keyword);
        int no = 1;
        for (Order o : list) {
            tableModel.addRow(new Object[] {
                no++,
                o.getNo(),
                o.getPelanggan(),
                "-",
                o.getUkuran(),
                o.getJumlah(),
                o.getBiaya(),
                o.getCreatedAt()
            });
        }
    }

    /**
     * Ringkasan harian: [totalTransaksi, totalPelanggan, totalOrder, totalOmzet]
     */
    public double[] getRingkasanHarian() {
        return orderDAO.getRingkasanHarian();
    }
}
