/*
 Navicat Premium Dump SQL

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80400 (8.4.0)
 Source Host           : localhost:3306
 Source Schema         : house

 Target Server Type    : MySQL
 Target Server Version : 80400 (8.4.0)
 File Encoding         : 65001

 Date: 11/12/2025 12:03:34
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
  CONSTRAINT `buyer_ibfk_1` FOREIGN KEY (`b_id`) REFERENCES `user` (`u_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '买方基础表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for contrast
-- ----------------------------
DROP TABLE IF EXISTS `contrast`;
CREATE TABLE `contrast`  (
  `c_id` int NOT NULL AUTO_INCREMENT,
  `c_buyer_id` int NOT NULL COMMENT '买方id',
  `c_house_id` int NOT NULL COMMENT '房源id',
  `c_total_price` double NULL DEFAULT NULL COMMENT '房源出售的总价格',
  `c_pay_way` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '付款方式（full：全款，installment：分期）',
  `c_paytime_ending` datetime NULL DEFAULT NULL COMMENT '买方付款截止日期',
  `c_paytime_actually` datetime NULL DEFAULT NULL COMMENT '买方付款实际日期',
  `c_delivery_ending` datetime NULL DEFAULT NULL COMMENT '卖方交房截止日期',
  `c_delivery_actually` datetime NULL DEFAULT NULL COMMENT '卖方交房实际日期',
  `c_buyer_agree` int NULL DEFAULT 0 COMMENT '买方同意（-1:拒绝,0:未处理,1同意）',
  `c_seller_agree` int NULL DEFAULT 0 COMMENT '卖方同意（-1:拒绝,0:未处理,1同意）',
  `c_paid` int NULL DEFAULT NULL COMMENT '买方是否付款（若为全款支付则视为支付总价，若分期支付则视为支付首付）',
  `c_delivered` int NULL DEFAULT NULL COMMENT '卖方是否交房',
  PRIMARY KEY (`c_id` DESC) USING BTREE,
  INDEX `c_buyer_id`(`c_buyer_id` ASC) USING BTREE,
  INDEX `c_house_id`(`c_house_id` ASC) USING BTREE,
  INDEX `c_total_price`(`c_total_price` ASC) USING BTREE,
  CONSTRAINT `contrast_ibfk_1` FOREIGN KEY (`c_buyer_id`) REFERENCES `buyer` (`b_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `contrast_ibfk_2` FOREIGN KEY (`c_house_id`) REFERENCES `house` (`h_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `check_argee` CHECK ((`c_buyer_agree` in (-(1),0,1)) and (`c_seller_agree` in (-(1),0,1))),
  CONSTRAINT `check_pay_way` CHECK (`c_pay_way` in (_utf8mb3'full',_utf8mb3'installment'))
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '合同基础表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '收藏表' ROW_FORMAT = DYNAMIC;

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
  `h_price` double NULL DEFAULT NULL COMMENT '房源每平方单价',
  `h_longitude` double NULL DEFAULT 0 COMMENT '房源经度',
  `h_latitude` double NULL DEFAULT 0 COMMENT '房源纬度',
  `h_square` double UNSIGNED NULL DEFAULT 0 COMMENT '建筑面积（单位：平方米）',
  `h_checked` int NULL DEFAULT 0 COMMENT '审核状态',
  PRIMARY KEY (`h_id` DESC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '房源基础表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '房源图片表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '房源标签关联表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '分期表' ROW_FORMAT = DYNAMIC;

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
  CONSTRAINT `seller_ibfk_1` FOREIGN KEY (`s_id`) REFERENCES `user` (`u_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '卖方基础表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`  (
  `t_id` int NOT NULL AUTO_INCREMENT,
  `t_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '标签名',
  PRIMARY KEY (`t_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '房源标签基础表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `u_id` int NOT NULL AUTO_INCREMENT,
  `u_type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `u_username` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `u_password` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`u_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户基础表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- View structure for view_contract
-- ----------------------------
DROP VIEW IF EXISTS `view_contract`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `view_contract` AS select `c`.`c_id` AS `vc_contract_id`,`c`.`c_buyer_id` AS `vc_buyer_id`,`c`.`c_house_id` AS `vc_house_id`,`h`.`h_seller_id` AS `vc_seller_id`,`c`.`c_total_price` AS `vc_total_price`,`c`.`c_pay_way` AS `vc_pay_way`,(case when ((`c`.`c_buyer_agree` = 1) and (`c`.`c_seller_agree` = 1)) then 1 else 0 end) AS `vc_agree_status`,(case when (((`c`.`c_pay_way` = 'full') and (`c`.`c_paid` = 1)) or ((`c`.`c_pay_way` = 'installment') and (`i`.`i_paid_count` >= `i`.`i_total_periods`))) then 2 when ((`c`.`c_pay_way` = 'installment') and (`i`.`i_paid_count` > 0) and (`i`.`i_paid_count` < `i`.`i_total_periods`)) then 1 when (((`c`.`c_pay_way` = 'full') and ((`c`.`c_paid` = 0) or (`c`.`c_paid` is null))) or ((`c`.`c_pay_way` = 'installment') and ((`i`.`i_paid_count` = 0) or (`i`.`i_paid_count` is null)))) then 0 else -(1) end) AS `vc_payment_status`,(case when ((((`c`.`c_pay_way` = 'full') and (`c`.`c_paid` = 1)) or ((`c`.`c_pay_way` = 'installment') and (`i`.`i_paid_count` >= `i`.`i_total_periods`))) and (`c`.`c_delivered` = 1)) then 1 else 0 end) AS `vc_completion_status`,`c`.`c_buyer_agree` AS `vc_buyer_agree`,`c`.`c_seller_agree` AS `vc_seller_agree`,`c`.`c_paid` AS `vc_paid`,`c`.`c_delivered` AS `vc_delivered`,`c`.`c_paytime_ending` AS `vc_paytime_ending`,`c`.`c_paytime_actually` AS `vc_paytime_actually`,`c`.`c_delivery_ending` AS `vc_delivery_ending`,`c`.`c_delivery_actually` AS `vc_delivery_actually` from ((`contrast` `c` left join `house` `h` on((`c`.`c_house_id` = `h`.`h_id`))) left join `installment` `i` on((`c`.`c_id` = `i`.`i_contrast_id`))) where (`h`.`h_seller_id` is not null);

-- ----------------------------
-- View structure for view_house
-- ----------------------------
DROP VIEW IF EXISTS `view_house`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `view_house` AS select `h`.`h_id` AS `vh_id`,`h`.`h_seller_id` AS `vh_seller_id`,`h`.`h_name` AS `vh_name`,`h`.`h_describe` AS `vh_describe`,`h`.`h_address` AS `vh_address`,`h`.`h_detail_address` AS `vh_detail_address`,`h`.`h_price` AS `vh_price`,`h`.`h_longitude` AS `vh_longitude`,`h`.`h_latitude` AS `vh_latitud`,`h`.`h_square` AS `vh_square`,`h`.`h_checked` AS `vh_checked`,round((`h`.`h_price` * `h`.`h_square`),2) AS `vh_total_price`,(case when exists(select 1 from (`contrast` `c` join `house` `h2` on((`c`.`c_house_id` = `h2`.`h_id`))) where ((`h2`.`h_id` = `h`.`h_id`) and (((`c`.`c_pay_way` = 'full') and (`c`.`c_paid` = 1) and (`c`.`c_delivered` = 1)) or ((`c`.`c_pay_way` = 'installment') and exists(select 1 from `installment` `i` where ((`i`.`i_contrast_id` = `c`.`c_id`) and (`i`.`i_paid_count` >= `i`.`i_total_periods`))) and (`c`.`c_delivered` = 1))))) then 0 when exists(select 1 from (`contrast` `c` join `house` `h2` on((`c`.`c_house_id` = `h2`.`h_id`))) where ((`h2`.`h_id` = `h`.`h_id`) and (`c`.`c_buyer_agree` = 1) and (`c`.`c_seller_agree` = 1) and (((`c`.`c_pay_way` = 'full') and ((`c`.`c_paid` = 0) or (`c`.`c_delivered` = 0))) or ((`c`.`c_pay_way` = 'installment') and exists(select 1 from `installment` `i` where ((`i`.`i_contrast_id` = `c`.`c_id`) and ((`i`.`i_paid_count` < `i`.`i_total_periods`) or (`c`.`c_delivered` = 0)))))))) then 2 else 1 end) AS `house_status` from `house` `h`;

-- ----------------------------
-- View structure for view_installment
-- ----------------------------
DROP VIEW IF EXISTS `view_installment`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `view_installment` AS select `i`.`i_contrast_id` AS `vi_contrast_id`,`i`.`i_down_payment` AS `vi_down_payment`,`i`.`i_total_periods` AS `vi_total_periods`,`i`.`i_paid_per_period` AS `vi_paid_per_period`,`i`.`i_paid_count` AS `i_paid_count`,round((`h`.`h_price` * `h`.`h_square`),2) AS `vi_total_price`,round(((`h`.`h_price` * `h`.`h_square`) - `i`.`i_down_payment`),2) AS `vi_final_payment`,round((((`h`.`h_price` * `h`.`h_square`) - `i`.`i_down_payment`) - (`i`.`i_paid_count` * `i`.`i_paid_per_period`)),2) AS `vi_paid_final`,(case when (`i`.`i_paid_count` = `i`.`i_total_periods`) then 1 else 0 end) AS `vi_payment_status` from ((`installment` `i` join `contrast` `c` on((`i`.`i_contrast_id` = `c`.`c_id`))) join `house` `h` on((`c`.`c_house_id` = `h`.`h_id`)));

SET FOREIGN_KEY_CHECKS = 1;
