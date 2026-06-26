package model;

import java.sql.Timestamp;

public class Diskon {

    private int id;
    private String nama;
    private double persen;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Diskon() {}

    public Diskon(int id, String nama, double persen,
                  Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.nama = nama;
        this.persen = persen;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public double getPersen() { return persen; }
    public void setPersen(double persen) { this.persen = persen; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
