package controller;

import dao.OrderDAO;
import java.time.LocalDate;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import model.Order;

public class ManagerController {

    private final OrderDAO orderDAO;

    public ManagerController() {
        this.orderDAO = new OrderDAO();
    }

    /**
     * Ringkasan untuk rentang tanggal tertentu.
     */
    public double[] getRingkasan(String startDate, String endDate) {
        if (startDate == null || endDate == null) {
            return orderDAO.getRingkasanHarian();
        }
        return orderDAO.getRingkasanByTanggal(startDate, endDate);
    }

    /**
     * Ringkasan bulan ini.
     */
    public double[] getRingkasanBulanIni() {
        LocalDate now = LocalDate.now();
        String start = now.withDayOfMonth(1).toString();
        String end = now.toString();
        return orderDAO.getRingkasanByTanggal(start, end);
    }

    /**
     * Ringkasan hari ini.
     */
    public double[] getRingkasanHarian() {
        return orderDAO.getRingkasanHarian();
    }

    /**
     * Load orders berdasarkan rentang tanggal.
     */
    public void loadOrders(DefaultTableModel model, String startDate, String endDate) {
        model.setRowCount(0);
        List<Order> orders;
        if (startDate == null || endDate == null) {
            orders = orderDAO.getAll();
        } else {
            orders = orderDAO.getByDateRange(startDate, endDate);
        }
        int no = 1;
        for (Order o : orders) {
            model.addRow(new Object[] {
                no++, o.getNo(), o.getPelanggan(), o.getUkuran(),
                o.getJumlah(), o.getBiaya(), o.getCreatedAt()
            });
        }
    }

    /**
     * Load semua orders.
     */
    public void loadAllOrders(DefaultTableModel model) {
        loadOrders(model, null, null);
    }
}
