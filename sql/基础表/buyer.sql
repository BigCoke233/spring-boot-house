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

 Date: 09/12/2025 20:12:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for buyer
-- ----------------------------
DROP TABLE IF EXISTS `buyer`;
CREATE TABLE `buyer`  (
  `b_id` int NOT NULL,
  `b_name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '姓名',
  `b_phone` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '电话',
  `b_email` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `b_mobile_assets` double NULL DEFAULT NULL COMMENT '移动资产数额',
  `b_fixed_assets` double NULL DEFAULT NULL COMMENT '固定资产数额',
  `b_annual_income` double NULL DEFAULT NULL COMMENT '年收入',
  PRIMARY KEY (`b_id`) USING BTREE,
  CONSTRAINT `buyer_ibfk_1` FOREIGN KEY (`b_id`) REFERENCES `user` (`u_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '买方基础表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
