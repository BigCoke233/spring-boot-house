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

 Date: 09/12/2025 20:12:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for seller
-- ----------------------------
DROP TABLE IF EXISTS `seller`;
CREATE TABLE `seller`  (
  `s_id` int NOT NULL,
  `s_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '公司名',
  `s_describe` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '公司描述',
  `s_phone` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '公司电话',
  `s_email` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '公司邮箱',
  `s_website` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '公司官网',
  PRIMARY KEY (`s_id`) USING BTREE,
  CONSTRAINT `seller_ibfk_1` FOREIGN KEY (`s_id`) REFERENCES `user` (`u_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '卖方基础表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
