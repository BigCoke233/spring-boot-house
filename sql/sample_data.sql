SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Clear existing data
-- ----------------------------
TRUNCATE TABLE `contract`;
TRUNCATE TABLE `house_picture`;
TRUNCATE TABLE `house_tag`;
TRUNCATE TABLE `tag`;
TRUNCATE TABLE `house`;
TRUNCATE TABLE `buyer`;
TRUNCATE TABLE `seller`;
DELETE FROM `user` WHERE u_id > 0;

-- ----------------------------
-- 1. Users (1 Admin, 2 Sellers, 2 Buyers)
-- Password is '123456' ($2a$10$AuhLKyieEJvChFTCE060qOhV5aB8XndkJqZK9tfwd62Zt4l19bofe)
-- ----------------------------
INSERT INTO `user` (`u_id`, `u_username`, `u_password`, `u_role`) VALUES
(1, 'admin', '$2a$10$AuhLKyieEJvChFTCE060qOhV5aB8XndkJqZK9tfwd62Zt4l19bofe', 'admin'),
(2, 'vanke_group', '$2a$10$AuhLKyieEJvChFTCE060qOhV5aB8XndkJqZK9tfwd62Zt4l19bofe', 'seller'),
(3, 'country_garden', '$2a$10$AuhLKyieEJvChFTCE060qOhV5aB8XndkJqZK9tfwd62Zt4l19bofe', 'seller'),
(4, 'zhangsan', '$2a$10$AuhLKyieEJvChFTCE060qOhV5aB8XndkJqZK9tfwd62Zt4l19bofe', 'buyer'),
(5, 'lisi', '$2a$10$AuhLKyieEJvChFTCE060qOhV5aB8XndkJqZK9tfwd62Zt4l19bofe', 'buyer');

-- ----------------------------
-- 2. Sellers
-- ----------------------------
INSERT INTO `seller` (`s_id`, `s_name`, `s_describe`, `s_phone`, `s_email`, `s_website`) VALUES
(2, '万科企业股份有限公司', '万科企业股份有限公司成立于1984年，经过三十余年的发展，已成为国内领先的城乡建设与生活服务商。', '0755-25606666', 'service@vanke.com', 'https://www.vanke.com'),
(3, '碧桂园控股有限公司', '碧桂园是为全世界创造美好生活产品的高科技综合性企业。', '0757-26600888', 'service@bgy.com.cn', 'https://www.bgy.com.cn');

-- ----------------------------
-- 3. Buyers
-- ----------------------------
INSERT INTO `buyer` (`b_id`, `b_name`, `b_phone`, `b_email`, `b_mobile_assets`, `b_fixed_assets`, `b_annual_income`) VALUES
(4, '张三', '13800138000', 'zhangsan@example.com', 5000000, 2000000, 800000),
(5, '李四', '13900139000', 'lisi@example.com', 3000000, 1000000, 500000);

-- ----------------------------
-- 4. Houses (Real coordinates across major Chinese cities)
-- ----------------------------
INSERT INTO `house` (`h_id`, `h_seller_id`, `h_name`, `h_describe`, `h_address`, `h_detail_address`, `h_price`, `h_longitude`, `h_latitude`, `h_square`, `h_checked`) VALUES
(1, 2, '万科翡翠滨江', '位于上海陆家嘴滨江金融城核心区，坐拥一线江景，尽享繁华。高端住宅，奢华装修，拎包入住。', '上海市浦东新区', '滨江大道999号', 150000, 121.5167, 31.2333, 180, 1),
(2, 2, '万科城市花园', '位于闵行区七宝商圈，成熟社区，配套齐全，环境优美，交通便利。适合家庭居住。', '上海市闵行区', '七莘路3333号', 85000, 121.3541, 31.1625, 120, 1),
(3, 3, '碧桂园云顶', '广州增城核心区域，背靠万亩山林，空气清新，天然氧吧。智能家居系统，科技生活。', '广州市增城区', '广汕公路北侧', 25000, 113.6253, 23.2849, 140, 1),
(4, 3, '碧桂园·十里银滩', '位于惠州亚婆角海滨旅游区，拥有十里银白沙滩，海景房，度假首选。', '惠州市惠东县', '亚婆角海滨旅游区', 12000, 114.7358, 22.7936, 90, 0),
(5, 2, '深圳湾1号', '深圳顶级豪宅，俯瞰深圳湾，尽享极致海景。顶级配套，私人管家服务，尊贵身份的象征。', '深圳市南山区', '东滨路与科苑大道交汇处', 200000, 113.9530, 22.5195, 250, 1),
(6, 3, '杭州壹号院', '位于杭州奥体中心旁，钱塘江畔，尽揽一线江景。现代风格设计，高端品质生活。', '杭州市滨江区', '飞虹路与奔竞大道交汇处', 90000, 120.2319, 30.2288, 160, 1),
(7, 2, '麓湖生态城', '成都天府新区核心，生态宜居大盘。湖光山色，公园环绕，回归自然的生活方式。', '成都市天府新区', '天府大道南一段', 40000, 104.0667, 30.5050, 130, 1),
(8, 2, '梵悦108', '北京CBD核心区域，国贸商圈，尽享繁华都市生活。高端公寓，艺术品位，精英首选。', '北京市朝阳区', '建国路乙118号', 160000, 116.4705, 39.9085, 110, 1),
(9, 3, '钟山国际高尔夫', '南京紫金山脚下，顶级高尔夫别墅区。私密性强，环境清幽，尊享贵族生活。', '南京市玄武区', '环陵路7号', 80000, 118.8687, 32.0620, 300, 1),
(10, 3, '苏州桃花源', '位于苏州金鸡湖畔，中式园林大宅。传承江南园林精髓，一步一景，虽由人作，宛自天开。', '苏州市吴中区', '金鸡湖大道', 110000, 120.6788, 31.2938, 400, 1);

-- ----------------------------
-- 5. House Pictures (Using Unsplash source URLs for reliability)
-- ----------------------------
INSERT INTO `house_picture` (`hp_house_id`, `hp_picture_number`, `hp_picture_path`) VALUES
-- House 1 (Shanghai - Luxury)
(1, 1, 'https://images.unsplash.com/photo-1600596542815-2a4d04774c13?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
(1, 2, 'https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
(1, 3, 'https://images.unsplash.com/photo-1600566753190-17f0baa2a6c3?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
-- House 2 (Shanghai - Residential)
(2, 1, 'https://images.unsplash.com/photo-1568605114967-8130f3a36994?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
(2, 2, 'https://images.unsplash.com/photo-1570129477492-45c003edd2be?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
-- House 3 (Guangzhou - Forest/Eco)
(3, 1, 'https://images.unsplash.com/photo-1576941089067-dbde23629277?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
(3, 2, 'https://images.unsplash.com/photo-1580587771525-78b9dba3b91d?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
(3, 3, 'https://images.unsplash.com/photo-1564013799919-ab600027ffc6?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
-- House 4 (Huizhou - Beach)
(4, 1, 'https://images.unsplash.com/photo-1512917774080-9991f1c4c750?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
-- House 5 (Shenzhen - Modern/Luxury)
(5, 1, 'https://images.unsplash.com/photo-1613490493576-7fde63acd811?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
(5, 2, 'https://images.unsplash.com/photo-1613977257363-707ba9348227?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
-- House 6 (Hangzhou - Modern)
(6, 1, 'https://images.unsplash.com/photo-1600585154340-be6161a56a0c?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
(6, 2, 'https://images.unsplash.com/photo-1600047509807-ba8f99d2cdde?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
-- House 7 (Chengdu - Green/Nature)
(7, 1, 'https://images.unsplash.com/photo-1605276374104-dee2a0ed3cd6?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
(7, 2, 'https://images.unsplash.com/photo-1605146769289-440113cc3d00?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
-- House 8 (Beijing - Apartment/City)
(8, 1, 'https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
-- House 9 (Nanjing - Villa)
(9, 1, 'https://images.unsplash.com/photo-1613545325278-f24b0cae1224?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
(9, 2, 'https://images.unsplash.com/photo-1613545325268-d27b923947c6?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
-- House 10 (Suzhou - Traditional/Garden)
(10, 1, 'https://images.unsplash.com/photo-1599809275671-b5942cabc7ad?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80'),
(10, 2, 'https://images.unsplash.com/photo-1600210492493-0946911123ea?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80');

-- ----------------------------
-- 6. Tags
-- ----------------------------
INSERT INTO `tag` (`t_id`, `t_name`) VALUES
(1, '江景'),
(2, '地铁房'),
(3, '学区房'),
(4, '精装修'),
(5, '随时看房'),
(6, '海景'),
(7, '豪宅'),
(8, '园林'),
(9, '别墅'),
(10, '公园');

-- ----------------------------
-- 7. House Tags
-- ----------------------------
INSERT INTO `house_tag` (`ht_house_id`, `ht_tag_id`) VALUES
(1, 1), (1, 2), (1, 4), (1, 7),
(2, 2), (2, 3), (2, 5),
(3, 4), (3, 5), (3, 10),
(4, 6), (4, 5),
(5, 1), (5, 6), (5, 7), (5, 4),
(6, 1), (6, 2), (6, 4),
(7, 10), (7, 5), (7, 4),
(8, 2), (8, 4), (8, 7),
(9, 9), (9, 8), (9, 7),
(10, 8), (10, 9), (10, 7);

-- ----------------------------
-- 8. Contracts
-- ----------------------------
INSERT INTO `contract` (`c_id`, `c_buyer_id`, `c_house_id`, `c_total_price`, `c_pay_way`, `c_paytime_ending`, `c_paytime_actually`, `c_delivery_ending`, `c_delivery_actually`, `c_buyer_agree`, `c_seller_agree`, `c_paid`, `c_delivered`) VALUES
(1, 4, 1, 27000000, 'full', '2025-06-01 00:00:00', NULL, '2025-07-01 00:00:00', NULL, 1, 1, 0, 0),
(2, 5, 3, 3500000, 'installment', '2025-05-01 00:00:00', '2025-04-15 10:30:00', '2025-06-01 00:00:00', NULL, 1, 0, 1, 0);

SET FOREIGN_KEY_CHECKS = 1;
