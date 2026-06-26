package model;

import java.sql.Timestamp;

public class Order {
    private int id;
    private String no;
    private String pelanggan;
    private String ukuran;
    private int jumlah;
    private double biaya;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Order() {}

    public Order(int id, String no, String pelanggan, String ukuran, int jumlah, double biaya,
                 Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.no = no;
        this.pelanggan = pelanggan;
        this.ukuran = ukuran;
        this.jumlah = jumlah;
        this.biaya = biaya;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNo() { return no; }
    public void setNo(String no) { this.no = no; }

    public String getPelanggan() { return pelanggan; }
    public void setPelanggan(String pelanggan) { this.pelanggan = pelanggan; }

    public String getUkuran() { return ukuran; }
    public void setUkuran(String ukuran) { this.ukuran = ukuran; }

    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }

    public double getBiaya() { return biaya; }
    public void setBiaya(double biaya) { this.biaya = biaya; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
