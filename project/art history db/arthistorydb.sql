-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: arthistorydb
-- ------------------------------------------------------
-- Server version	5.7.18-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `art_piece`
--

DROP TABLE IF EXISTS `art_piece`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `art_piece` (
  `piece_name` varchar(50) NOT NULL,
  `artist` varchar(30) DEFAULT NULL,
  `exhibit` varchar(50) DEFAULT NULL,
  `date_acquired` int(11) DEFAULT NULL,
  PRIMARY KEY (`piece_name`),
  KEY `exhibit_fk` (`exhibit`),
  CONSTRAINT `exhibit_fk` FOREIGN KEY (`exhibit`) REFERENCES `exhibit` (`ename`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `art_piece`
--

LOCK TABLES `art_piece` WRITE;
/*!40000 ALTER TABLE `art_piece` DISABLE KEYS */;
INSERT INTO `art_piece` VALUES ('Brutus','Michelangelo','high renaissance',22016),('David','Michelangelo','high renaissance',42017),('Guernica','Pablo Picasso','modern',52000),('Hope II','Gustav Klimt','modern',102006),('Le Parlement','Claude Monet','modern',112008),('Les Demoisselles d\'Avignon','Pablo Picasso','modern',122000),('Poppies','Claude Monet','modern',92012),('The Elephants','Salvador Dali','modern',62003),('The Persistence of Memory','Salvador Dali','modern',112003);
/*!40000 ALTER TABLE `art_piece` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `artist`
--

DROP TABLE IF EXISTS `artist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `artist` (
  `aname` varchar(30) NOT NULL,
  `yob` int(11) NOT NULL,
  `yod` int(11) DEFAULT NULL,
  `period` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`aname`),
  KEY `period_fk` (`period`),
  CONSTRAINT `period_fk` FOREIGN KEY (`period`) REFERENCES `period` (`pname`) 
  ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `artist`
--

LOCK TABLES `artist` WRITE;
/*!40000 ALTER TABLE `artist` DISABLE KEYS */;
INSERT INTO `artist` VALUES ('Claude Monet',1840,1926,'Impressionism'),('Gustav Klimt',1862,1918,'Modern'),('Michelangelo',1475,1564,'Renaissance'),('Pablo Picasso',1881,1973,'Cubism'),('Salvador Dali',1904,1989,'Surrealism');
/*!40000 ALTER TABLE `artist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `artist_creates_piece`
--

DROP TABLE IF EXISTS `artist_creates_piece`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `artist_creates_piece` (
  `artist_name` varchar(30) NOT NULL,
  `piece_name` varchar(50) NOT NULL,
  KEY `artist_fk` (`artist_name`),
  KEY `piece_fk` (`piece_name`),
  CONSTRAINT `artist_fk` FOREIGN KEY (`artist_name`) REFERENCES `artist` (`aname`) ON UPDATE CASCADE,
  CONSTRAINT `piece_fk` FOREIGN KEY (`piece_name`) REFERENCES `art_piece` (`piece_name`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `artist_creates_piece`
--

LOCK TABLES `artist_creates_piece` WRITE;
/*!40000 ALTER TABLE `artist_creates_piece` DISABLE KEYS */;
/*!40000 ALTER TABLE `artist_creates_piece` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `curator`
--

DROP TABLE IF EXISTS `curator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `curator` (
  `cname` varchar(30) NOT NULL,
  `salary` int(11) NOT NULL,
  PRIMARY KEY (`cname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `curator`
--

LOCK TABLES `curator` WRITE;
/*!40000 ALTER TABLE `curator` DISABLE KEYS */;
INSERT INTO `curator` VALUES ('James',4);
/*!40000 ALTER TABLE `curator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exhibit`
--

DROP TABLE IF EXISTS `exhibit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `exhibit` (
  `ename` varchar(50) NOT NULL,
  `start_date` int(11) DEFAULT NULL,
  `end_date` int(11) DEFAULT NULL,
  `curator` varchar(20) NOT NULL,
  `wing_of_museum` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`ename`),
  KEY `curator_fk` (`curator`),
  CONSTRAINT `curator_fk` FOREIGN KEY (`curator`) REFERENCES `curator` (`cname`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exhibit`
--

LOCK TABLES `exhibit` WRITE;
/*!40000 ALTER TABLE `exhibit` DISABLE KEYS */;
INSERT INTO `exhibit` VALUES ('high renaissance',52017,92017,'James','west'),('modern',122012,122017,'James','north');
/*!40000 ALTER TABLE `exhibit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `museum`
--

DROP TABLE IF EXISTS `museum`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `museum` (
  `address` varchar(50) NOT NULL,
  `museum_name` varchar(50) NOT NULL,
  `phone_number` int(11) DEFAULT NULL,
  PRIMARY KEY (`address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `museum`
--

LOCK TABLES `museum` WRITE;
/*!40000 ALTER TABLE `museum` DISABLE KEYS */;
/*!40000 ALTER TABLE `museum` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `museum_has_exhibit`
--

DROP TABLE IF EXISTS `museum_has_exhibit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `museum_has_exhibit` (
  `museum_address` varchar(50) NOT NULL,
  `exhibit` varchar(50) NOT NULL,
  KEY `museum_address_fk` (`museum_address`),
  KEY `m_exhibit_fk` (`exhibit`),
  CONSTRAINT `m_exhibit_fk` FOREIGN KEY (`exhibit`) REFERENCES `exhibit` (`ename`) ON UPDATE CASCADE,
  CONSTRAINT `museum_address_fk` FOREIGN KEY (`museum_address`) REFERENCES `museum` (`address`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `museum_has_exhibit`
--

LOCK TABLES `museum_has_exhibit` WRITE;
/*!40000 ALTER TABLE `museum_has_exhibit` DISABLE KEYS */;
/*!40000 ALTER TABLE `museum_has_exhibit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `period`
--

DROP TABLE IF EXISTS `period`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `period` (
  `pname` varchar(20) NOT NULL,
  `start_date` int(11) NOT NULL,
  `end_date` int(11) NOT NULL,
  PRIMARY KEY (`pname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `period`
--

LOCK TABLES `period` WRITE;
/*!40000 ALTER TABLE `period` DISABLE KEYS */;
INSERT INTO `period` VALUES ('Cubism',1907,1920),('Impressionism',1870,1880),('Modern',1860,1970),('Renaissance',1300,1700),('Surrealism',1920,1930),('Contemporary',1960,2017);
/*!40000 ALTER TABLE `period` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-06-20 23:02:14
