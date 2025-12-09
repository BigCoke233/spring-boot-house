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

 Date: 09/12/2025 20:58:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for follow
-- ----------------------------
DROP TABLE IF EXISTS `follow`;
CREATE TABLE `follow`  (
  `f_buyer_id` int NOT NULL COMMENT '收藏者id',
  `f_house_id` int NOT NULL COMMENT '收藏房源id',
  PRIMARY KEY (`f_buyer_id`, `f_house_id`) USING BTREE,
  INDEX `f_house_id`(`f_house_id` ASC) USING BTREE,
  CONSTRAINT `follow_ibfk_1` FOREIGN KEY (`f_buyer_id`) REFERENCES `buyer` (`b_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `follow_ibfk_2` FOREIGN KEY (`f_house_id`) REFERENCES `house` (`h_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '收藏表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
