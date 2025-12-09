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

 Date: 09/12/2025 20:58:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for contrast
-- ----------------------------
DROP TABLE IF EXISTS `contrast`;
CREATE TABLE `contrast`  (
  `c_id` int NOT NULL AUTO_INCREMENT,
  `c_buyer_id` int NOT NULL COMMENT '买方id',
  `c_house_id` int NOT NULL COMMENT '房源id',
  `c_total_price` double NULL DEFAULT NULL COMMENT '房源出售的总价格',
  `c_pay_way` enum('full','installment') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '付款方式（full：全款，installment：分期）',
  `c_paytime_ending` datetime NULL DEFAULT NULL COMMENT '买方付款截止日期',
  `c_paytime_actually` datetime NULL DEFAULT NULL COMMENT '买方付款实际日期',
  `c_delivery_ending` datetime NULL DEFAULT NULL COMMENT '卖方交房截止日期',
  `c_delivery_actually` datetime NULL DEFAULT NULL COMMENT '卖方交房实际日期',
  `c_buyer_agree` int NULL DEFAULT NULL COMMENT '买方同意（-1:拒绝,0:未处理,1同意）',
  `c_seller_agree` int NULL DEFAULT NULL COMMENT '卖方同意（-1:拒绝,0:未处理,1同意）',
  `c_paid` int NULL DEFAULT NULL COMMENT '买方是否付款（若为全款支付则视为支付总价，若分期支付则视为支付首付）',
  `c_delivered` int NULL DEFAULT NULL COMMENT '卖方是否交房',
  PRIMARY KEY (`c_id`) USING BTREE,
  INDEX `c_buyer_id`(`c_buyer_id` ASC) USING BTREE,
  INDEX `c_house_id`(`c_house_id` ASC) USING BTREE,
  INDEX `c_total_price`(`c_total_price` ASC) USING BTREE,
  CONSTRAINT `contrast_ibfk_1` FOREIGN KEY (`c_buyer_id`) REFERENCES `buyer` (`b_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `contrast_ibfk_2` FOREIGN KEY (`c_house_id`) REFERENCES `house` (`h_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '合同基础表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
