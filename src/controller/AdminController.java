package controller;

import dao.DiskonDAO;
import dao.MetodeBayarDAO;
import dao.OrderDAO;
import dao.PaketPhotoDAO;
import dao.UserDAO;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import model.Diskon;
import model.MetodeBayar;
import model.Order;
import model.PaketPhoto;
import model.User;

public class AdminController {

    private final UserDAO userDAO;
    private final OrderDAO orderDAO;
    private final PaketPhotoDAO paketDAO;
    private final DiskonDAO diskonDAO;
    private final MetodeBayarDAO metodeBayarDAO;

    public AdminController() {
        this.userDAO = new UserDAO();
        this.orderDAO = new OrderDAO();
        this.paketDAO = new PaketPhotoDAO();
        this.diskonDAO = new DiskonDAO();
        this.metodeBayarDAO = new MetodeBayarDAO();
    }

    // ==================== User Management ====================

    public List<User> getAllUsers() {
        return userDAO.getAll();
    }

    public boolean insertUser(String username, String password, String nama, String role) {
        User u = new User();
        u.setUsername(username);
        u.setPassword(password);
        u.setNama(nama);
        u.setRole(role);
        return userDAO.insert(u);
    }

    public boolean updateUser(int id, String username, String password, String nama, String role) {
        User u = new User();
        u.setId(id);
        u.setUsername(username);
        u.setPassword(password);
        u.setNama(nama);
        u.setRole(role);
        return userDAO.update(u);
    }

    public boolean deleteUser(int id) {
        return userDAO.delete(id);
    }

    public void loadUsers(DefaultTableModel model) {
        model.setRowCount(0);
        List<User> users = userDAO.getAll();
        int no = 1;
        for (User u : users) {
            model.addRow(new Object[] {
                no++, u.getId(), u.getUsername(), u.getNama(), u.getRole(), u.getCreatedAt()
            });
        }
    }

    // ==================== Paket Photo ====================

    public void loadPaket(DefaultTableModel model) {
        model.setRowCount(0);
        List<PaketPhoto> list = paketDAO.getAll();
        int no = 1;
        for (PaketPhoto p : list) {
            model.addRow(new Object[] {
                no++, p.getId(), p.getNamaPaket(), p.getUkuran(), p.getHarga()
            });
        }
    }

    public boolean insertPaket(String namaPaket, String ukuran, double harga) {
        PaketPhoto p = new PaketPhoto();
        p.setNamaPaket(namaPaket);
        p.setUkuran(ukuran);
        p.setHarga(harga);
        return paketDAO.insert(p);
    }

    public boolean updatePaket(int id, String namaPaket, String ukuran, double harga) {
        PaketPhoto p = new PaketPhoto();
        p.setId(id);
        p.setNamaPaket(namaPaket);
        p.setUkuran(ukuran);
        p.setHarga(harga);
        return paketDAO.update(p);
    }

    public boolean deletePaket(int id) {
        return paketDAO.delete(id);
    }

    // ==================== Diskon ====================

    public void loadDiskon(DefaultTableModel model) {
        model.setRowCount(0);
        List<Diskon> list = diskonDAO.getAll();
        int no = 1;
        for (Diskon d : list) {
            model.addRow(new Object[] {
                no++, d.getId(), d.getNama(), d.getPersen() + "%"
            });
        }
    }

    public boolean insertDiskon(String nama, double persen) {
        Diskon d = new Diskon();
        d.setNama(nama);
        d.setPersen(persen);
        return diskonDAO.insert(d);
    }

    public boolean updateDiskon(int id, String nama, double persen) {
        Diskon d = new Diskon();
        d.setId(id);
        d.setNama(nama);
        d.setPersen(persen);
        return diskonDAO.update(d);
    }

    public boolean deleteDiskon(int id) {
        return diskonDAO.delete(id);
    }

    // ==================== Metode Bayar ====================

    public void loadMetodeBayar(DefaultTableModel model) {
        model.setRowCount(0);
        List<MetodeBayar> list = metodeBayarDAO.getAll();
        int no = 1;
        for (MetodeBayar m : list) {
            model.addRow(new Object[] {
                no++, m.getId(), m.getNama()
            });
        }
    }

    public boolean insertMetodeBayar(String nama) {
        MetodeBayar m = new MetodeBayar();
        m.setNama(nama);
        return metodeBayarDAO.insert(m);
    }

    public boolean updateMetodeBayar(int id, String nama) {
        MetodeBayar m = new MetodeBayar();
        m.setId(id);
        m.setNama(nama);
        return metodeBayarDAO.update(m);
    }

    public boolean deleteMetodeBayar(int id) {
        return metodeBayarDAO.delete(id);
    }

    // ==================== Order & Summary ====================

    public void loadAllOrders(DefaultTableModel model) {
        model.setRowCount(0);
        List<Order> orders = orderDAO.getAll();
        int no = 1;
        for (Order o : orders) {
            model.addRow(new Object[] {
                no++, o.getNo(), o.getPelanggan(), o.getUkuran(),
                o.getJumlah(), o.getBiaya(), o.getCreatedAt()
            });
        }
    }

    public double[] getRingkasanHarian() {
        return orderDAO.getRingkasanHarian();
    }

    public int getUserCount() {
        return userDAO.getAll().size();
    }
}
