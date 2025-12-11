/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80037 (8.0.37)
 Source Host           : localhost:3306
 Source Schema         : commercial_house_sales_management

 Target Server Type    : MySQL
 Target Server Version : 80037 (8.0.37)
 File Encoding         : 65001

 Date: 10/12/2025 10:01:13
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for house_picture
-- ----------------------------
DROP TABLE IF EXISTS `house_picture`;
CREATE TABLE `house_picture`  (
  `hp_house_id` int NOT NULL COMMENT '房源id',
  `hp_picture_number` int NOT NULL COMMENT '房源图片编号',
  `hp_picture_path` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '房源图片路径',
  PRIMARY KEY (`hp_house_id`, `hp_picture_number`) USING BTREE,
  CONSTRAINT `house_picture_ibfk_1` FOREIGN KEY (`hp_house_id`) REFERENCES `house` (`h_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '房源图片表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
