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

 Date: 10/12/2025 10:01:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for house_tag
-- ----------------------------
DROP TABLE IF EXISTS `house_tag`;
CREATE TABLE `house_tag`  (
  `ht_house_id` int NOT NULL COMMENT '房源id',
  `ht_tag_id` int NOT NULL COMMENT '标签id',
  PRIMARY KEY (`ht_house_id`, `ht_tag_id`) USING BTREE,
  INDEX `ht_tag_id`(`ht_tag_id` ASC) USING BTREE,
  CONSTRAINT `house_tag_ibfk_1` FOREIGN KEY (`ht_house_id`) REFERENCES `house` (`h_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `house_tag_ibfk_2` FOREIGN KEY (`ht_tag_id`) REFERENCES `tag` (`t_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '房源标签关联表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
