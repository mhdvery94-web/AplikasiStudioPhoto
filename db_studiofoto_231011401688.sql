-- MySQL dump 10.13  Distrib 8.4.3, for Win64 (x86_64)
--
-- Host: localhost    Database: db_studiofoto_231011401688
-- ------------------------------------------------------
-- Server version	8.4.3

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `diskon`
--

DROP TABLE IF EXISTS `diskon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `diskon` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nama` varchar(100) NOT NULL,
  `persen` double NOT NULL DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nama` (`nama`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diskon`
--

LOCK TABLES `diskon` WRITE;
/*!40000 ALTER TABLE `diskon` DISABLE KEYS */;
INSERT INTO `diskon` VALUES (1,'Tanpa Diskon',0,'2026-07-01 01:38:39','2026-07-01 01:38:39'),(2,'Diskon 5%',5,'2026-07-01 01:38:39','2026-07-01 01:38:39'),(3,'Diskon 10%',10,'2026-07-01 01:38:39','2026-07-01 01:38:39'),(4,'Diskon 20%',20,'2026-07-01 01:38:39','2026-07-01 01:38:39');
/*!40000 ALTER TABLE `diskon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `metode_bayar`
--

DROP TABLE IF EXISTS `metode_bayar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `metode_bayar` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nama` varchar(100) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nama` (`nama`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `metode_bayar`
--

LOCK TABLES `metode_bayar` WRITE;
/*!40000 ALTER TABLE `metode_bayar` DISABLE KEYS */;
INSERT INTO `metode_bayar` VALUES (1,'Cash','2026-07-01 01:38:41','2026-07-01 01:38:41'),(2,'Transfer','2026-07-01 01:38:41','2026-07-01 01:38:41'),(3,'QRIS','2026-07-01 01:38:41','2026-07-01 01:38:41'),(4,'Debit','2026-07-01 01:38:41','2026-07-01 01:38:41');
/*!40000 ALTER TABLE `metode_bayar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `no` varchar(50) NOT NULL,
  `pelanggan` varchar(100) NOT NULL,
  `ukuran` varchar(20) NOT NULL,
  `jumlah` int NOT NULL,
  `biaya` double NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `no` (`no`),
  KEY `idx_no` (`no`),
  KEY `idx_pelanggan` (`pelanggan`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'ORD-20260614-0001','Andi Wijaya','4x6',10,20000,'2026-06-15 07:58:29','2026-06-15 07:58:29'),(2,'ORD-20260614-0002','Rina Kusuma','3x4',20,20000,'2026-06-15 07:58:29','2026-06-15 07:58:29'),(3,'ORD-20260614-0003','Dedi Permana','A4',5,50000,'2026-06-15 07:58:29','2026-06-15 07:58:29'),(4,'ORD-20260614-0004','Maya Sari','2x3',50,25000,'2026-06-15 07:58:29','2026-06-15 07:58:29'),(5,'ORD-20260614-0005','Hendra Gunawan','10x15',8,60000,'2026-06-15 07:58:29','2026-06-15 07:58:29'),(6,'ORD-20260614-0006','Lina Marlina','5x7',15,52500,'2026-06-15 07:58:29','2026-06-15 07:58:29'),(7,'ORD-20260615-0007','Agnes','2x3',100,50000,'2026-06-15 08:03:58','2026-06-15 08:03:58');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paket_photo`
--

DROP TABLE IF EXISTS `paket_photo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `paket_photo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nama_paket` varchar(100) NOT NULL,
  `ukuran` varchar(50) NOT NULL,
  `harga` double NOT NULL DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_paket_ukuran` (`nama_paket`,`ukuran`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paket_photo`
--

LOCK TABLES `paket_photo` WRITE;
/*!40000 ALTER TABLE `paket_photo` DISABLE KEYS */;
/*!40000 ALTER TABLE `paket_photo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `role` enum('Admin','Kasir','Kepala Toko') NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','admin123','Administrator','Admin','2026-06-15 07:58:27','2026-06-15 07:58:27'),(2,'kasir1','kasir123','Budi Santoso','Kasir','2026-06-15 07:58:27','2026-06-15 07:58:27'),(3,'kasir2','kasir123','Siti Nurhaliza','Kasir','2026-06-15 07:58:27','2026-06-15 07:58:27'),(4,'kepala','kepala123','Ahmad Dahlan','Kepala Toko','2026-06-15 07:58:27','2026-06-15 07:58:27');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-01 14:42:25
