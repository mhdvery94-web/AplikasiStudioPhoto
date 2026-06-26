-- =============================================
-- Master Data Tables untuk Studio Photo
-- Database: db_studiofoto_231011401688
-- =============================================

-- Tabel Paket Photo (nama_paket, ukuran, harga)
CREATE TABLE IF NOT EXISTS paket_photo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama_paket VARCHAR(50) NOT NULL,
    ukuran VARCHAR(20) NOT NULL,
    harga DOUBLE NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uq_paket_ukuran (nama_paket, ukuran)
) ENGINE=InnoDB;

-- Tabel Diskon
CREATE TABLE IF NOT EXISTS diskon (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(50) NOT NULL UNIQUE,
    persen DOUBLE NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Tabel Metode Bayar
CREATE TABLE IF NOT EXISTS metode_bayar (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- =============================================
-- Seed data (sesuai yang hardcoded sebelumnya)
-- =============================================

INSERT INTO paket_photo (nama_paket, ukuran, harga) VALUES
    ('Paket Basic', '3R', 25000),
    ('Paket Basic', '4R', 30000),
    ('Paket Basic', '5R', 37500),
    ('Paket Basic', '10R', 50000),
    ('Paket Basic', 'A4', 62500),
    ('Paket Basic', 'A3', 87500),
    ('Paket Standard', '3R', 50000),
    ('Paket Standard', '4R', 60000),
    ('Paket Standard', '5R', 75000),
    ('Paket Standard', '10R', 100000),
    ('Paket Standard', 'A4', 125000),
    ('Paket Standard', 'A3', 175000),
    ('Paket Premium', '3R', 75000),
    ('Paket Premium', '4R', 90000),
    ('Paket Premium', '5R', 112500),
    ('Paket Premium', '10R', 150000),
    ('Paket Premium', 'A4', 187500),
    ('Paket Premium', 'A3', 262500),
    ('Paket Exclusive', '3R', 100000),
    ('Paket Exclusive', '4R', 120000),
    ('Paket Exclusive', '5R', 150000),
    ('Paket Exclusive', '10R', 200000),
    ('Paket Exclusive', 'A4', 250000),
    ('Paket Exclusive', 'A3', 350000)
ON DUPLICATE KEY UPDATE harga = VALUES(harga);

INSERT INTO diskon (nama, persen) VALUES
    ('Tanpa Diskon', 0),
    ('Diskon 5%', 5),
    ('Diskon 10%', 10),
    ('Diskon 15%', 15),
    ('Diskon 20%', 20)
ON DUPLICATE KEY UPDATE persen = VALUES(persen);

INSERT INTO metode_bayar (nama) VALUES
    ('Cash'),
    ('Transfer'),
    ('QRIS')
ON DUPLICATE KEY UPDATE nama = VALUES(nama);
