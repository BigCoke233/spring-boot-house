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

 Date: 09/12/2025 21:00:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for house
-- ----------------------------
DROP TABLE IF EXISTS `house`;
CREATE TABLE `house`  (
  `h_id` int NOT NULL AUTO_INCREMENT,
  `h_seller_id` int NOT NULL COMMENT '卖方id',
  `h_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '房源名字',
  `h_describe` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '房源详情',
  `h_address` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '房源地址',
  `h_detail_address` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '房源门牌号',
  `h_price` double NULL DEFAULT NULL COMMENT '房源推荐价格',
  `h_longitude` double NULL DEFAULT 0 COMMENT '房源经度',
  `h_latitude` double NULL DEFAULT 0 COMMENT '房源纬度',
  `h_square` double UNSIGNED NULL DEFAULT 0 COMMENT '建筑面积（单位：平方米）',
  `h_checked` int NULL DEFAULT 0 COMMENT '审核状态',
  PRIMARY KEY (`h_id` DESC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '房源基础表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
