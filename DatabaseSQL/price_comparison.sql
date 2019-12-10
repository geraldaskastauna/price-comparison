--
-- Database: `price_comparison`
--
DROP DATABASE IF EXISTS `price_comparison`;
CREATE DATABASE `price_comparison` DEFAULT CHARACTER SET utf8;
USE `price_comparison`;
-- --------------------------------------------------------

--
-- Table structure for table `product`
--
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`
(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `brand` varchar(255) NOT NULL,
  `description` text NOT NULL,
  `image_url` LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `url`
--
DROP TABLE IF EXISTS `url`;
CREATE TABLE `url`
(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `domain` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `query_string` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `laptop`
--
DROP TABLE IF EXISTS `laptop`;
CREATE TABLE `laptop`
(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `price` float NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_url_id` (`url_id`),
  KEY `FK_product_id` (`product_id`),
  CONSTRAINT `FK_url_id` FOREIGN KEY (`url_id`) REFERENCES `url` (`id`),
  CONSTRAINT `FK_product_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------
