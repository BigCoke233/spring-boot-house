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

 Date: 09/12/2025 20:59:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for installment
-- ----------------------------
DROP TABLE IF EXISTS `installment`;
CREATE TABLE `installment`  (
  `i_contrast_id` int NOT NULL COMMENT '合同id',
  `i_total_price` double NOT NULL COMMENT '合同总价格',
  `i_down_payment` double NULL DEFAULT NULL COMMENT '首付',
  `i_total_periods` int NULL DEFAULT NULL COMMENT '总期数',
  `i_paid_per_period` double NULL DEFAULT NULL COMMENT '每期还款额',
  `i_paid_count` int NULL DEFAULT NULL COMMENT '已还款期数',
  PRIMARY KEY (`i_contrast_id`) USING BTREE,
  INDEX `i_total_price`(`i_total_price` ASC) USING BTREE,
  CONSTRAINT `installment_ibfk_1` FOREIGN KEY (`i_contrast_id`) REFERENCES `contrast` (`c_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `installment_ibfk_2` FOREIGN KEY (`i_total_price`) REFERENCES `contrast` (`c_total_price`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '分期表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
