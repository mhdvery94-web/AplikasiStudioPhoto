package model;

import java.sql.Timestamp;

public class PaketPhoto {

    private int id;
    private String namaPaket;
    private String ukuran;
    private double harga;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public PaketPhoto() {}

    public PaketPhoto(int id, String namaPaket, String ukuran, double harga,
                      Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.namaPaket = namaPaket;
        this.ukuran = ukuran;
        this.harga = harga;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNamaPaket() { return namaPaket; }
    public void setNamaPaket(String namaPaket) { this.namaPaket = namaPaket; }

    public String getUkuran() { return ukuran; }
    public void setUkuran(String ukuran) { this.ukuran = ukuran; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
