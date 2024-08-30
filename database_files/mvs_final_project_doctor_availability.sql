CREATE DATABASE  IF NOT EXISTS `mvs_final_project` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `mvs_final_project`;
-- MySQL dump 10.13  Distrib 8.0.36, for macos14 (arm64)
--
-- Host: localhost    Database: mvs_final_project
-- ------------------------------------------------------
-- Server version	8.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `doctor_availability`
--

DROP TABLE IF EXISTS `doctor_availability`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctor_availability` (
  `availability_id` int NOT NULL AUTO_INCREMENT,
  `doctor_user_id` int NOT NULL,
  `date` date NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `Is_Available` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`availability_id`),
  KEY `fk_doctor_user_id` (`doctor_user_id`),
  CONSTRAINT `fk_doctor_user_id` FOREIGN KEY (`doctor_user_id`) REFERENCES `doctor` (`User_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3233 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor_availability`
--

LOCK TABLES `doctor_availability` WRITE;
/*!40000 ALTER TABLE `doctor_availability` DISABLE KEYS */;
INSERT INTO `doctor_availability` VALUES (3194,2,'2024-05-13','10:15:00','12:00:00',1),(3195,2,'2024-05-13','02:00:00','03:15:00',1),(3196,2,'2024-05-13','03:30:00','04:15:00',1),(3197,2,'2024-05-14','08:00:00','10:00:00',1),(3198,2,'2024-05-14','10:15:00','12:00:00',1),(3199,2,'2024-05-14','01:00:00','03:00:00',1),(3200,2,'2024-05-14','03:30:00','04:15:00',1),(3201,2,'2024-05-15','08:00:00','10:00:00',0),(3202,2,'2024-05-15','10:15:00','12:00:00',1),(3203,2,'2024-05-15','01:00:00','03:00:00',1),(3204,2,'2024-05-15','03:30:00','04:15:00',1),(3206,2,'2024-05-16','10:15:00','12:00:00',1),(3207,2,'2024-05-16','01:00:00','03:00:00',1),(3208,2,'2024-05-16','03:30:00','04:15:00',1),(3209,2,'2024-05-17','08:00:00','10:00:00',1),(3210,2,'2024-05-17','10:15:00','12:00:00',1),(3211,2,'2024-05-17','01:00:00','03:00:00',1),(3212,2,'2024-05-17','03:30:00','04:15:00',1),(3214,1025,'2024-05-13','11:00:00','12:45:00',1),(3219,1025,'2024-05-16','08:15:00','10:15:00',1),(3220,1025,'2024-05-16','11:00:00','12:45:00',1),(3221,1025,'2024-05-17','08:15:00','10:15:00',1),(3222,1025,'2024-05-17','11:00:00','12:45:00',1),(3223,2,'2024-05-20','01:00:00','03:00:00',1),(3224,2,'2024-05-20','03:15:00','04:00:00',1),(3225,2,'2024-05-21','01:00:00','03:00:00',1),(3226,2,'2024-05-21','04:00:00','05:00:00',1),(3227,2,'2024-05-22','01:00:00','03:00:00',1),(3228,2,'2024-05-22','04:00:00','05:00:00',1),(3229,2,'2024-05-23','01:00:00','03:00:00',1),(3230,2,'2024-05-23','04:00:00','05:00:00',1),(3231,2,'2024-05-24','01:00:00','03:00:00',1),(3232,2,'2024-05-24','04:00:00','05:00:00',1);
/*!40000 ALTER TABLE `doctor_availability` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `CheckTimeOverlapBeforeInsert` BEFORE INSERT ON `doctor_availability` FOR EACH ROW BEGIN
    -- Check for any overlapping time slots
    IF EXISTS (
        SELECT 1 FROM doctor_availability da
        WHERE da.doctor_user_id = NEW.doctor_user_id
        AND da.date = NEW.date
        AND (
            (NEW.start_time < da.end_time AND NEW.start_time >= da.start_time)
            OR
            (NEW.end_time > da.start_time AND NEW.end_time <= da.end_time)
            OR
            (NEW.start_time <= da.start_time AND NEW.end_time >= da.end_time)
        )
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Overlapping time slots are not allowed.';
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `CheckTimeOverlapBeforeUpdate` BEFORE UPDATE ON `doctor_availability` FOR EACH ROW BEGIN
    -- Check for any overlapping time slots excluding the current record
    IF EXISTS (
        SELECT 1 FROM doctor_availability da
        WHERE da.availability_id != OLD.availability_id -- Exclude current record
        AND da.doctor_user_id = NEW.doctor_user_id
        AND da.date = NEW.date
        AND (
            (NEW.start_time < da.end_time AND NEW.start_time >= da.start_time)
            OR
            (NEW.end_time > da.start_time AND NEW.end_time <= da.end_time)
            OR
            (NEW.start_time <= da.start_time AND NEW.end_time >= da.end_time)
        )
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Overlapping time slots are not allowed.';
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-13  9:13:48
