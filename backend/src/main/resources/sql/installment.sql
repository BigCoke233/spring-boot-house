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

 Date: 10/12/2025 10:01:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for installment
-- ----------------------------
DROP TABLE IF EXISTS `installment`;
CREATE TABLE `installment`  (
  `i_contrast_id` int NOT NULL COMMENT '合同id',
  `i_down_payment` double NOT NULL COMMENT '首付',
  `i_total_periods` int NULL DEFAULT NULL COMMENT '总期数',
  `i_paid_per_period` double NULL DEFAULT NULL COMMENT '每期还款额',
  `i_paid_count` int NULL DEFAULT NULL COMMENT '已还款期数',
  PRIMARY KEY (`i_contrast_id`) USING BTREE,
  CONSTRAINT `installment_ibfk_1` FOREIGN KEY (`i_contrast_id`) REFERENCES `contrast` (`c_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `check_pay_count` CHECK (`i_paid_count` <= `i_total_periods`)
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '分期表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
