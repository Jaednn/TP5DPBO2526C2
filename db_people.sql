-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 17, 2026 at 10:12 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_people`
--

-- --------------------------------------------------------

--
-- Table structure for table `person`
--

CREATE TABLE `person` (
  `id` varchar(20) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `thn_lahir` int(11) NOT NULL,
  `kategori` varchar(50) NOT NULL,
  `status` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `person`
--

INSERT INTO `person` (`id`, `nama`, `thn_lahir`, `kategori`, `status`) VALUES
('EMP001', 'Farrel Zafh', 2002, 'Staff', 'Tetap'),
('EMP002', 'Farez Ghazali', 2001, 'Staff', 'Tetap'),
('EMP003', 'Devindra Dwi', 2003, 'Intern', 'Tidak Tetap'),
('EMP004', 'Jadzil Maliki', 2000, 'Manager', 'Tetap'),
('EMP005', 'Fadey Putra', 2004, 'Intern', 'Tidak Tetap'),
('EMP006', 'Juna Juniga', 2002, 'Staff', 'Tidak Tetap'),
('EMP007', 'Tenxi Ghifari', 2001, 'Supervisor', 'Tetap'),
('EMP008', 'Adrian khalif', 2003, 'Staff', 'Tetap'),
('EMP010', 'Farez TM', 2004, 'Staff', 'Tetap');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `person`
--
ALTER TABLE `person`
  ADD PRIMARY KEY (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
