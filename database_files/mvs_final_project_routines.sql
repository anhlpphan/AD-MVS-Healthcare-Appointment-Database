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
-- Temporary view structure for view `appointmentsbydoctor`
--

DROP TABLE IF EXISTS `appointmentsbydoctor`;
/*!50001 DROP VIEW IF EXISTS `appointmentsbydoctor`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `appointmentsbydoctor` AS SELECT 
 1 AS `Appointment_ID`,
 1 AS `Patient_User_ID`,
 1 AS `Doctor_User_ID`,
 1 AS `Location`,
 1 AS `Branch_No`,
 1 AS `Start_Time`,
 1 AS `End_Time`,
 1 AS `Date`,
 1 AS `Doctor_ID`,
 1 AS `Doctor_First_Name`,
 1 AS `Doctor_Last_Name`,
 1 AS `Patient_ID`,
 1 AS `Patient_First_Name`,
 1 AS `Patient_Last_Name`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `insurancedetails`
--

DROP TABLE IF EXISTS `insurancedetails`;
/*!50001 DROP VIEW IF EXISTS `insurancedetails`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `insurancedetails` AS SELECT 
 1 AS `User_ID`,
 1 AS `First_Name`,
 1 AS `Last_Name`,
 1 AS `Insurance_Card_No`,
 1 AS `Insurance_Company_Name`,
 1 AS `Insurance_Company_Phone_No`,
 1 AS `Exp_Date`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `patientprofile`
--

DROP TABLE IF EXISTS `patientprofile`;
/*!50001 DROP VIEW IF EXISTS `patientprofile`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `patientprofile` AS SELECT 
 1 AS `User_ID`,
 1 AS `First_Name`,
 1 AS `Last_Name`,
 1 AS `Date_of_Birth`,
 1 AS `Sex`,
 1 AS `Phone_No`,
 1 AS `Email`,
 1 AS `Street`,
 1 AS `City`,
 1 AS `State`,
 1 AS `Zip_Code`,
 1 AS `Height`,
 1 AS `Weight`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `manageroversight`
--

DROP TABLE IF EXISTS `manageroversight`;
/*!50001 DROP VIEW IF EXISTS `manageroversight`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `manageroversight` AS SELECT 
 1 AS `User_ID`,
 1 AS `First_Name`,
 1 AS `Last_Name`,
 1 AS `Department_Supervised_ID`,
 1 AS `Department_Name`,
 1 AS `Branch_Name`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `doctorinformation`
--

DROP TABLE IF EXISTS `doctorinformation`;
/*!50001 DROP VIEW IF EXISTS `doctorinformation`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `doctorinformation` AS SELECT 
 1 AS `User_ID`,
 1 AS `First_Name`,
 1 AS `Last_Name`,
 1 AS `Specialization`,
 1 AS `Years_Of_Experience`,
 1 AS `Phone_No`,
 1 AS `Email`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `upcomingappointments`
--

DROP TABLE IF EXISTS `upcomingappointments`;
/*!50001 DROP VIEW IF EXISTS `upcomingappointments`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `upcomingappointments` AS SELECT 
 1 AS `Appointment_ID`,
 1 AS `Patient_User_ID`,
 1 AS `Date`,
 1 AS `Start_Time`,
 1 AS `End_Time`,
 1 AS `Patient_First_Name`,
 1 AS `Patient_Last_Name`,
 1 AS `Doctor_First_Name`,
 1 AS `Doctor_Last_Name`,
 1 AS `Appointment_Location`,
 1 AS `Branch_Name`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `patientmedicalrecords`
--

DROP TABLE IF EXISTS `patientmedicalrecords`;
/*!50001 DROP VIEW IF EXISTS `patientmedicalrecords`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `patientmedicalrecords` AS SELECT 
 1 AS `User_ID`,
 1 AS `First_Name`,
 1 AS `Last_Name`,
 1 AS `Record_ID`,
 1 AS `Description`,
 1 AS `Date`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `appointmentsbydoctor`
--

/*!50001 DROP VIEW IF EXISTS `appointmentsbydoctor`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `appointmentsbydoctor` AS select `a`.`Appointment_ID` AS `Appointment_ID`,`a`.`Patient_User_ID` AS `Patient_User_ID`,`a`.`Doctor_User_ID` AS `Doctor_User_ID`,`a`.`Location` AS `Location`,`b`.`Branch_No` AS `Branch_No`,`a`.`Start_Time` AS `Start_Time`,`a`.`End_Time` AS `End_Time`,`a`.`Date` AS `Date`,`d`.`User_ID` AS `Doctor_ID`,`u`.`First_Name` AS `Doctor_First_Name`,`u`.`Last_Name` AS `Doctor_Last_Name`,`p`.`User_ID` AS `Patient_ID`,`pu`.`First_Name` AS `Patient_First_Name`,`pu`.`Last_Name` AS `Patient_Last_Name` from (((((`appointment` `a` join `doctor` `d` on((`a`.`Doctor_User_ID` = `d`.`User_ID`))) join `user` `u` on((`d`.`User_ID` = `u`.`User_ID`))) join `patient` `p` on((`a`.`Patient_User_ID` = `p`.`User_ID`))) join `user` `pu` on((`p`.`User_ID` = `pu`.`User_ID`))) join `mvs_branch` `b` on((`a`.`Location` = `b`.`Location`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `insurancedetails`
--

/*!50001 DROP VIEW IF EXISTS `insurancedetails`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `insurancedetails` AS select `p`.`User_ID` AS `User_ID`,`u`.`First_Name` AS `First_Name`,`u`.`Last_Name` AS `Last_Name`,`i`.`Insurance_Card_No` AS `Insurance_Card_No`,`i`.`Insurance_Company_Name` AS `Insurance_Company_Name`,`i`.`Insurance_Company_Phone_No` AS `Insurance_Company_Phone_No`,`i`.`Exp_Date` AS `Exp_Date` from ((`patient` `p` join `user` `u` on((`p`.`User_ID` = `u`.`User_ID`))) join `insurance` `i` on((`p`.`User_ID` = `i`.`User_ID`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `patientprofile`
--

/*!50001 DROP VIEW IF EXISTS `patientprofile`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `patientprofile` AS select `u`.`User_ID` AS `User_ID`,`u`.`First_Name` AS `First_Name`,`u`.`Last_Name` AS `Last_Name`,`u`.`Date_of_Birth` AS `Date_of_Birth`,`u`.`Sex` AS `Sex`,`u`.`Phone_No` AS `Phone_No`,`u`.`Email` AS `Email`,`u`.`Street` AS `Street`,`u`.`City` AS `City`,`u`.`State` AS `State`,`u`.`Zip_Code` AS `Zip_Code`,`p`.`Height` AS `Height`,`p`.`Weight` AS `Weight` from (`user` `u` join `patient` `p` on((`u`.`User_ID` = `p`.`User_ID`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `manageroversight`
--

/*!50001 DROP VIEW IF EXISTS `manageroversight`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `manageroversight` AS select `u`.`User_ID` AS `User_ID`,`u`.`First_Name` AS `First_Name`,`u`.`Last_Name` AS `Last_Name`,`m`.`Department_Supervised_ID` AS `Department_Supervised_ID`,`d`.`Department_Name` AS `Department_Name`,`b`.`Branch_Name` AS `Branch_Name` from (((`manager` `m` join `user` `u` on((`m`.`User_ID` = `u`.`User_ID`))) join `department` `d` on((`m`.`Department_Supervised_ID` = `d`.`Department_No`))) join `mvs_branch` `b` on((`d`.`Branch_No` = `b`.`Branch_No`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `doctorinformation`
--

/*!50001 DROP VIEW IF EXISTS `doctorinformation`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `doctorinformation` AS select `u`.`User_ID` AS `User_ID`,`u`.`First_Name` AS `First_Name`,`u`.`Last_Name` AS `Last_Name`,`d`.`Specialization` AS `Specialization`,`d`.`Years_Of_Experience` AS `Years_Of_Experience`,`u`.`Phone_No` AS `Phone_No`,`u`.`Email` AS `Email` from (`user` `u` join `doctor` `d` on((`u`.`User_ID` = `d`.`User_ID`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `upcomingappointments`
--

/*!50001 DROP VIEW IF EXISTS `upcomingappointments`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `upcomingappointments` AS select `a`.`Appointment_ID` AS `Appointment_ID`,`a`.`Patient_User_ID` AS `Patient_User_ID`,`a`.`Date` AS `Date`,`a`.`Start_Time` AS `Start_Time`,`a`.`End_Time` AS `End_Time`,`pat`.`First_Name` AS `Patient_First_Name`,`pat`.`Last_Name` AS `Patient_Last_Name`,`doc`.`First_Name` AS `Doctor_First_Name`,`doc`.`Last_Name` AS `Doctor_Last_Name`,`b`.`Location` AS `Appointment_Location`,`b`.`Branch_Name` AS `Branch_Name` from (((((`appointment` `a` join `patient` `p` on((`a`.`Patient_User_ID` = `p`.`User_ID`))) join `user` `pat` on((`p`.`User_ID` = `pat`.`User_ID`))) join `doctor` `d` on((`a`.`Doctor_User_ID` = `d`.`User_ID`))) join `user` `doc` on((`d`.`User_ID` = `doc`.`User_ID`))) join `mvs_branch` `b` on((`a`.`Location` = `b`.`Location`))) where (`a`.`Date` >= curdate()) order by `a`.`Date`,`a`.`Start_Time` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `patientmedicalrecords`
--

/*!50001 DROP VIEW IF EXISTS `patientmedicalrecords`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `patientmedicalrecords` AS select `p`.`User_ID` AS `User_ID`,`u`.`First_Name` AS `First_Name`,`u`.`Last_Name` AS `Last_Name`,`mr`.`Record_ID` AS `Record_ID`,`mr`.`Description` AS `Description`,`mr`.`Date` AS `Date` from ((`patient` `p` join `user` `u` on((`p`.`User_ID` = `u`.`User_ID`))) join `medical_records` `mr` on((`p`.`User_ID` = `mr`.`User_ID`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Dumping events for database 'mvs_final_project'
--

--
-- Dumping routines for database 'mvs_final_project'
--
/*!50003 DROP PROCEDURE IF EXISTS `fill_calendar_dates` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `fill_calendar_dates`()
BEGIN
  DECLARE v_start_date DATE DEFAULT CURDATE();
  DECLARE v_end_date DATE DEFAULT DATE_FORMAT(CURDATE() + INTERVAL 1 YEAR - INTERVAL 1 DAY, '%Y-%m-%d');

  WHILE v_start_date <= v_end_date DO
    INSERT INTO calendar_dates (date) VALUES (v_start_date);
    SET v_start_date = v_start_date + INTERVAL 1 DAY;
  END WHILE;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `fill_doctor_time_slots` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `fill_doctor_time_slots`()
BEGIN
  DECLARE finished INT DEFAULT FALSE;
  DECLARE d_date DATE;
  DECLARE cur CURSOR FOR 
    SELECT date 
    FROM calendar_dates
    WHERE WEEKDAY(date) < 5; -- 0 = Monday, 4 = Friday
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = TRUE;

  OPEN cur;

  read_loop: LOOP
    FETCH cur INTO d_date;
    IF finished THEN
      LEAVE read_loop;
    END IF;

    -- Inserting time slots for each doctor
    INSERT INTO doctor_availability (doctor_user_id, date, start_time, end_time) VALUES
      (9, d_date, '08:00:00', '10:00:00'),
      (9, d_date, '10:15:00', '12:00:00'),
      (9, d_date, '12:15:00', '14:00:00'),
      (9, d_date, '14:15:00', '16:00:00'),
      (11, d_date, '08:00:00', '10:00:00'),
      (11, d_date, '10:15:00', '12:00:00'),
      (11, d_date, '12:15:00', '14:00:00'),
      (11, d_date, '14:15:00', '16:00:00'),
      (13, d_date, '08:00:00', '10:00:00'),
      (13, d_date, '10:15:00', '12:00:00'),
      (13, d_date, '12:15:00', '14:00:00'),
      (13, d_date, '14:15:00', '16:00:00');
  END LOOP;

  CLOSE cur;
END ;;
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

-- Dump completed on 2024-05-13  9:13:49
