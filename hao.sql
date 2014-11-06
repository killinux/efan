-- MySQL dump 10.13  Distrib 5.5.29, for Linux (i686)
--
-- Host: localhost    Database: ott_launcher_market_dev
-- ------------------------------------------------------
-- Server version	5.5.29-log

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
-- Table structure for table `ott_app`
--

DROP TABLE IF EXISTS `ott_app`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ott_app` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL,
  `pkg_name` varchar(128) DEFAULT NULL,
  `ver_name` varchar(128) DEFAULT NULL,
  `ver_code` bigint(20) DEFAULT NULL,
  `pub_time` datetime DEFAULT NULL,
  `pub_status` varchar(1) DEFAULT 'P',
  `pkg_size` decimal(7,2) DEFAULT NULL,
  `brief` varchar(2048) DEFAULT NULL,
  `update_content` varchar(2048) DEFAULT NULL,
  `poster_url` varchar(256) DEFAULT NULL,
  `icon_url` varchar(256) DEFAULT NULL,
  `snapshots_urls` varchar(2048) DEFAULT NULL,
  `support_controllers` varchar(256) DEFAULT NULL,
  `developer` varchar(128) DEFAULT NULL,
  `apk_url` varchar(256) DEFAULT NULL,
  `download_count` int(11) DEFAULT NULL,
  `is_deleted` tinyint(4) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_user` int(11) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_user` int(11) DEFAULT NULL,
  `lastest_ver` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `app_pkg_index` (`pkg_name`,`ver_code`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ott_app`
--

LOCK TABLES `ott_app` WRITE;
/*!40000 ALTER TABLE `ott_app` DISABLE KEYS */;
INSERT INTO `ott_app` VALUES (1,'每日瑜伽','com.dailyyoga.tv','2.0',3,NULL,'T',6.77,'asdfasdf','','/57fe46/eccbc8/poster_194F36A8M9OSK_logo.png',NULL,'/57fe46/eccbc8/snapshots_194F36DG7GRQP_海报1.png,/57fe46/eccbc8/snapshots_194F36DS1DDF3_海报2.png,/57fe46/eccbc8/snapshots_194F36EOSQ7E2_海报3.png','标准遥控器','asdf','/57fe46/eccbc8/apk_194F366A2F387_dailyyogaTv.apk',0,0,'2014-10-17 15:57:36',1,NULL,NULL,NULL),(2,'优酷','com.youku.phone','4.1.2',58,'2014-10-20 14:50:57','P',21.14,'asdf','','/c9f04c/66f041/poster_194F378C55O5S_123.png',NULL,'/c9f04c/66f041/snapshots_194F37C6KU0IF_234.png,/c9f04c/66f041/snapshots_194F37CPHT3OJ_345.png,/c9f04c/66f041/snapshots_194F37DF28J6V_567.png','空中飞鼠','asdf','/c9f04c/66f041/apk_194F3741CA5IB_com.youku.4.1.2.apk',0,0,'2014-10-17 15:58:05',1,NULL,1,58),(5,'百度地图','com.baidu.BaiduMap.tv','1.1.0',10,'2014-10-20 14:51:00','P',6.47,'asdfsda','','/c57300/d3d944/poster_194M2F4DJR7TE_logo.png',NULL,'/c57300/d3d944/snapshots_194M2F83DHSMN_海报1.png,/c57300/d3d944/snapshots_194M2F8C3PFQT_海报2.png,/c57300/d3d944/snapshots_194M2F8PQE5FG_海报3.png','空中飞鼠','asdf','/c57300/d3d944/apk_194M2F2D7M3AS_baiduditu.apk',0,0,'2014-10-20 14:01:27',1,NULL,1,10),(6,'优酷','com.youku.phone','4.3',63,NULL,'T',21.95,'asdf','','/c9f04c/66f041/poster_194F378C55O5S_123.png',NULL,'/c9f04c/66f041/snapshots_194F37C6KU0IF_234.png,/c9f04c/66f041/snapshots_194F37CPHT3OJ_345.png,/c9f04c/66f041/snapshots_194F37DF28J6V_567.png','空中飞鼠','asdf','/c9f04c/03afdb/apk_194M3HOV1TQ8C_com.youku.4.3.apk',0,0,'2014-10-20 14:20:07',1,NULL,NULL,58),(7,'春雨医生','me.chunyu.ChunyuDoctor','5.4.2',542,NULL,'T',8.11,'ASDF','ASDF','/029d27/7dcd34/poster_194OLEHBGMDB0.png',NULL,'/029d27/7dcd34/snapshots_194OLF0RICC1F.png,/029d27/7dcd34/snapshots_194OLF0T5E18P.png,/029d27/7dcd34/snapshots_194OLF1J04GHQ.png','标准遥控器','ASDF','/029d27/7dcd34/apk_194OL9KI1UFD.apk',0,0,'2014-10-21 14:12:03',1,'2014-10-21 14:14:22',1,NULL);
/*!40000 ALTER TABLE `ott_app` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ott_app_catagory`
--

DROP TABLE IF EXISTS `ott_app_catagory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ott_app_catagory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '0',
  `is_deleted` tinyint(4) DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `create_user` int(11) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ott_app_catagory`
--

LOCK TABLES `ott_app_catagory` WRITE;
/*!40000 ALTER TABLE `ott_app_catagory` DISABLE KEYS */;
INSERT INTO `ott_app_catagory` VALUES (1,'asdfsdf',1,0,'2014-10-17 15:56:36',1,'2014-10-17 15:57:11',1),(2,'ffffff',1,0,'2014-10-17 15:56:40',1,'2014-10-17 15:57:06',1);
/*!40000 ALTER TABLE `ott_app_catagory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ott_app_channel`
--

DROP TABLE IF EXISTS `ott_app_channel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ott_app_channel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL,
  `weight` int(11) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `create_user` int(11) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_user` int(11) DEFAULT NULL,
  `is_deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ott_app_channel`
--

LOCK TABLES `ott_app_channel` WRITE;
/*!40000 ALTER TABLE `ott_app_channel` DISABLE KEYS */;
INSERT INTO `ott_app_channel` VALUES (1,'1',1,1,1,'2014-10-17 15:56:51',1,0),(2,'2',2,1,1,'2014-10-17 15:56:58',1,0);
/*!40000 ALTER TABLE `ott_app_channel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ott_catagory_app_rel`
--

DROP TABLE IF EXISTS `ott_catagory_app_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ott_catagory_app_rel` (
  `app_id` int(11) DEFAULT NULL,
  `catagory_id` int(11) DEFAULT NULL,
  UNIQUE KEY `catagory_app_index` (`app_id`,`catagory_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ott_catagory_app_rel`
--

LOCK TABLES `ott_catagory_app_rel` WRITE;
/*!40000 ALTER TABLE `ott_catagory_app_rel` DISABLE KEYS */;
INSERT INTO `ott_catagory_app_rel` VALUES (1,1),(2,2),(3,2),(4,2),(5,2),(6,2),(7,1);
/*!40000 ALTER TABLE `ott_catagory_app_rel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ott_channel_app_rel`
--

DROP TABLE IF EXISTS `ott_channel_app_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ott_channel_app_rel` (
  `app_id` int(11) DEFAULT NULL,
  `channel_id` int(11) DEFAULT NULL,
  `app_weight` int(11) DEFAULT '1',
  UNIQUE KEY `app_channel_index` (`app_id`,`channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ott_channel_app_rel`
--

LOCK TABLES `ott_channel_app_rel` WRITE;
/*!40000 ALTER TABLE `ott_channel_app_rel` DISABLE KEYS */;
INSERT INTO `ott_channel_app_rel` VALUES (1,2,1),(2,1,1),(3,1,1),(4,1,1),(5,1,1),(6,1,1),(7,2,1);
/*!40000 ALTER TABLE `ott_channel_app_rel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `display_name` varchar(64) NOT NULL,
  `login_name` varchar(64) NOT NULL,
  `password` varchar(32) NOT NULL,
  `role` tinyint(4) NOT NULL,
  `is_deleted` tinyint(4) DEFAULT '0',
  `email` varchar(128) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_user` int(11) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_name_UNIQUE` (`login_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'魏永林','weiyl','21218cca77804d2ba1922c33e0151105',1,0,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-11-06 11:16:40
