-- phpMyAdmin SQL Dump
-- version 4.6.5.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 03, 2018 at 04:15 AM
-- Server version: 10.1.21-MariaDB
-- PHP Version: 5.6.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `aset_kendaraan`
--

-- --------------------------------------------------------

--
-- Table structure for table `tb_asset`
--

CREATE TABLE `tb_asset` (
  `plat_nomor` varchar(10) NOT NULL,
  `id_bahan_bakar` varchar(10) NOT NULL,
  `jenis_asset` varchar(25) NOT NULL,
  `nama_asset` varchar(25) NOT NULL,
  `kondisi_asset` varchar(25) NOT NULL,
  `kepemilikan_asset` varchar(25) NOT NULL,
  `status_asset` varchar(10) NOT NULL,
  `km_awal` int(10) NOT NULL,
  `km_akhir` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_asset`
--

INSERT INTO `tb_asset` (`plat_nomor`, `id_bahan_bakar`, `jenis_asset`, `nama_asset`, `kondisi_asset`, `kepemilikan_asset`, `status_asset`, `km_awal`, `km_akhir`) VALUES
('B6452SA', 'BB002', 'Mobil', 'AVANZA', 'Layak Pakai', 'Milik Perusahaan', 'Siap Pakai', 10, 10),
('G3543GA', 'BB001', 'Mobil', 'AYLA', 'Layak Pakai', 'Milik Perusahaan', 'Siap Pakai', 7, 7),
('G8573QA', 'BB003', 'Motor', 'MEGAPRO', 'Layak Pakai', 'Pinjaman', 'Siap Pakai', 600, 600),
('K2242SF', 'BB002', 'Motor', 'VARIO', 'Layak Pakai', 'Milik Perusahaan', 'Siap Pakai', 11, 11);

-- --------------------------------------------------------

--
-- Table structure for table `tb_bahan_bakar`
--

CREATE TABLE `tb_bahan_bakar` (
  `id_bahan_bakar` varchar(10) NOT NULL,
  `nama_bahan_bakar` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_bahan_bakar`
--

INSERT INTO `tb_bahan_bakar` (`id_bahan_bakar`, `nama_bahan_bakar`) VALUES
('BB001', 'PREMIUM'),
('BB002', 'PERTALITE'),
('BB003', 'PERTAMAX'),
('BB004', 'SOLAR');

-- --------------------------------------------------------

--
-- Table structure for table `tb_peminjaman`
--

CREATE TABLE `tb_peminjaman` (
  `id_peminjaman` int(10) NOT NULL,
  `id_user` varchar(10) NOT NULL,
  `plat_nomor` varchar(10) NOT NULL,
  `nama_peminjam` varchar(25) NOT NULL,
  `divisi` varchar(255) NOT NULL,
  `telepon` varchar(14) NOT NULL,
  `tanggal_peminjaman` date NOT NULL,
  `keterangan` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tb_pengembalian`
--

CREATE TABLE `tb_pengembalian` (
  `id_pengembalian` int(10) NOT NULL,
  `id_peminjaman` int(10) NOT NULL,
  `id_user` varchar(10) NOT NULL,
  `km_akhir` int(100) NOT NULL,
  `tanggal_pengembalian` date NOT NULL,
  `tagihan` int(255) NOT NULL,
  `nota` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tb_user`
--

CREATE TABLE `tb_user` (
  `id_user` varchar(10) NOT NULL,
  `nama_user` varchar(25) NOT NULL,
  `telepon` varchar(25) NOT NULL,
  `alamat` varchar(255) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `level` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_user`
--

INSERT INTO `tb_user` (`id_user`, `nama_user`, `telepon`, `alamat`, `username`, `password`, `level`) VALUES
('U001', 'taufiq', '087830106027', 'pemalang', 'taufiq', 'taufiq', 'Admin'),
('U002', 'kamila', '082313987883', 'medan', 'kamila', 'kamila', 'Operator'),
('U003', 'rima', '087830106027', 'kudus', 'rima', 'rima', 'Operator');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tb_asset`
--
ALTER TABLE `tb_asset`
  ADD PRIMARY KEY (`plat_nomor`),
  ADD KEY `id_bahan_bakar` (`id_bahan_bakar`);

--
-- Indexes for table `tb_bahan_bakar`
--
ALTER TABLE `tb_bahan_bakar`
  ADD PRIMARY KEY (`id_bahan_bakar`);

--
-- Indexes for table `tb_peminjaman`
--
ALTER TABLE `tb_peminjaman`
  ADD PRIMARY KEY (`id_peminjaman`),
  ADD KEY `id_user` (`id_user`),
  ADD KEY `id_plat_nomor` (`plat_nomor`);

--
-- Indexes for table `tb_pengembalian`
--
ALTER TABLE `tb_pengembalian`
  ADD PRIMARY KEY (`id_pengembalian`),
  ADD KEY `id_pengembalian` (`id_pengembalian`),
  ADD KEY `id_peminjaman` (`id_peminjaman`),
  ADD KEY `id_user` (`id_user`);

--
-- Indexes for table `tb_user`
--
ALTER TABLE `tb_user`
  ADD PRIMARY KEY (`id_user`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tb_peminjaman`
--
ALTER TABLE `tb_peminjaman`
  MODIFY `id_peminjaman` int(10) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `tb_pengembalian`
--
ALTER TABLE `tb_pengembalian`
  MODIFY `id_pengembalian` int(10) NOT NULL AUTO_INCREMENT;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `tb_asset`
--
ALTER TABLE `tb_asset`
  ADD CONSTRAINT `tb_asset_ibfk_1` FOREIGN KEY (`id_bahan_bakar`) REFERENCES `tb_bahan_bakar` (`id_bahan_bakar`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tb_peminjaman`
--
ALTER TABLE `tb_peminjaman`
  ADD CONSTRAINT `tb_peminjaman_ibfk_4` FOREIGN KEY (`plat_nomor`) REFERENCES `tb_asset` (`plat_nomor`),
  ADD CONSTRAINT `tb_peminjaman_ibfk_5` FOREIGN KEY (`id_user`) REFERENCES `tb_user` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tb_pengembalian`
--
ALTER TABLE `tb_pengembalian`
  ADD CONSTRAINT `tb_pengembalian_ibfk_2` FOREIGN KEY (`id_peminjaman`) REFERENCES `tb_peminjaman` (`id_peminjaman`),
  ADD CONSTRAINT `tb_pengembalian_ibfk_3` FOREIGN KEY (`id_user`) REFERENCES `tb_user` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
