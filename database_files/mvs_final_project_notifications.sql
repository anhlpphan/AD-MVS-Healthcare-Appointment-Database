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
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `Notification_ID` int NOT NULL AUTO_INCREMENT,
  `Patient_User_ID` int DEFAULT NULL,
  `Doctor_User_ID` int DEFAULT NULL,
  `Date` date DEFAULT NULL,
  `Start_Time` time DEFAULT NULL,
  `End_Time` time DEFAULT NULL,
  `Message` varchar(255) DEFAULT NULL,
  `Status` enum('Read','Unread') DEFAULT NULL,
  `Timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`Notification_ID`),
  KEY `Patient_User_ID` (`Patient_User_ID`),
  KEY `Doctor_User_ID` (`Doctor_User_ID`),
  CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`Patient_User_ID`) REFERENCES `patient` (`User_ID`),
  CONSTRAINT `notifications_ibfk_2` FOREIGN KEY (`Doctor_User_ID`) REFERENCES `doctor` (`User_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES (60,3,2,'2024-05-11','14:50:00','17:00:00','Appointment rescheduled.','Unread','2024-05-10 18:28:40'),(61,3,2,'2024-05-11','12:30:00','14:25:00','Appointment rescheduled.','Unread','2024-05-10 18:33:31'),(62,3,2,'2024-05-11','14:50:00','17:00:00','Appointment rescheduled.','Unread','2024-05-10 18:39:48'),(63,3,2,'2024-05-11','12:30:00','14:25:00','Appointment rescheduled.','Unread','2024-05-10 18:41:45'),(64,3,2,'2024-05-11','12:30:00','14:25:00','Appointment rescheduled.','Unread','2024-05-10 18:49:36'),(65,3,2,'2024-05-11','08:00:00','10:00:00','Appointment rescheduled.','Unread','2024-05-10 18:51:25'),(66,3,2,'2024-05-11','08:00:00','10:00:00','Appointment rescheduled.','Unread','2024-05-10 18:54:02'),(67,3,2,'2024-05-11','12:30:00','14:25:00','Appointment rescheduled.','Unread','2024-05-10 18:55:08'),(68,3,2,'2024-05-11','12:30:00','14:25:00','Appointment rescheduled.','Unread','2024-05-10 18:57:34'),(69,3,2,'2024-05-11','14:50:00','17:00:00','Appointment rescheduled.','Unread','2024-05-10 19:08:10'),(70,3,2,'2024-05-11','08:00:00','10:00:00','Appointment rescheduled.','Unread','2024-05-10 19:10:42'),(71,3,2,'2024-05-12','08:00:00','12:00:00','Appointment rescheduled.','Unread','2024-05-10 19:11:50'),(72,3,2,'2024-05-11','12:30:00','14:25:00','Appointment rescheduled.','Unread','2024-05-10 19:12:08'),(73,3,2,'2024-05-11','12:30:00','14:25:00','Appointment rescheduled.','Unread','2024-05-10 19:55:56'),(74,3,2,'2024-05-12','08:00:00','12:00:00','Appointment rescheduled.','Unread','2024-05-10 20:04:21'),(75,3,2,'2024-05-12','08:00:00','12:00:00','Appointment rescheduled.','Unread','2024-05-10 20:06:08'),(76,3,2,'2024-05-11','14:50:00','17:00:00','Appointment rescheduled.','Unread','2024-05-11 00:47:42'),(77,3,2,'2024-05-11','08:00:00','10:00:00','Appointment rescheduled.','Unread','2024-05-11 00:50:44'),(78,3,2,'2024-05-11','08:00:00','10:00:00','Appointment rescheduled.','Unread','2024-05-11 01:11:58'),(79,3,2,'2024-05-12','08:00:00','12:00:00','Appointment rescheduled.','Unread','2024-05-11 01:13:40'),(80,3,2,'2024-05-12','08:00:00','12:00:00','Appointment rescheduled.','Unread','2024-05-11 01:18:45'),(81,3,2,'2024-05-11','12:30:00','14:25:00','Appointment rescheduled.','Unread','2024-05-11 01:19:37'),(82,3,2,'2024-05-11','14:50:00','17:00:00','Appointment rescheduled.','Unread','2024-05-11 01:20:05'),(83,3,2,'2024-05-11','08:00:00','10:00:00','Appointment rescheduled.','Unread','2024-05-11 14:14:18'),(84,3,2,'2024-05-11','14:50:00','17:00:00','Appointment rescheduled.','Unread','2024-05-11 14:55:36'),(85,3,2,'2024-05-11','08:00:00','10:00:00','Appointment rescheduled.','Unread','2024-05-11 14:57:41'),(86,3,2,'2024-05-11','12:30:00','14:25:00','Appointment rescheduled.','Unread','2024-05-11 14:59:30'),(88,3,2,'2024-05-11','08:00:00','10:00:00','Appointment rescheduled.','Unread','2024-05-11 15:33:47'),(89,3,2,'2024-05-11','12:30:00','14:25:00','Appointment rescheduled.','Unread','2024-05-11 15:42:07'),(90,3,2,'2024-05-11','08:00:00','10:00:00','Appointment rescheduled.','Unread','2024-05-11 15:45:43'),(91,3,2,'2024-05-11','14:50:00','17:00:00','Appointment rescheduled.','Unread','2024-05-11 15:55:50'),(92,3,2,'2024-05-11','08:00:00','10:00:00','Appointment rescheduled.','Unread','2024-05-11 15:55:57'),(93,3,2,'2024-05-11','12:30:00','14:25:00','Appointment rescheduled.','Unread','2024-05-11 15:56:18'),(94,3,2,'2024-05-12','08:00:00','12:00:00','Appointment rescheduled.','Unread','2024-05-11 17:24:50'),(95,3,2,'2024-05-11','08:00:00','10:00:00','Appointment rescheduled.','Unread','2024-05-11 17:25:25'),(96,3,2,'2024-05-11','14:50:00','17:00:00','Appointment rescheduled.','Unread','2024-05-11 17:25:39'),(97,3,2,'2024-05-11','15:50:00','17:00:00','Appointment rescheduled.','Unread','2024-05-11 17:39:27'),(98,3,2,'2024-05-12','08:00:00','12:00:00','Appointment rescheduled.','Unread','2024-05-11 20:29:56'),(99,3,2,'2024-05-11','12:30:00','14:25:00','Appointment rescheduled.','Unread','2024-05-11 20:30:32'),(100,3,2,'2024-05-12','08:00:00','12:00:00','Appointment rescheduled.','Unread','2024-05-11 20:30:37'),(101,3,2,'2024-05-11','14:50:00','17:00:00','Appointment rescheduled.','Unread','2024-05-11 20:30:46'),(102,3,2,'2024-05-15','10:15:00','12:45:00','Appointment rescheduled.','Unread','2024-05-12 18:14:02');
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-13  9:13:48
