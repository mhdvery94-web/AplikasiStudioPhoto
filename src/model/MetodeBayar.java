package model;

import java.sql.Timestamp;

public class MetodeBayar {

    private int id;
    private String nama;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public MetodeBayar() {}

    public MetodeBayar(int id, String nama, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.nama = nama;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
