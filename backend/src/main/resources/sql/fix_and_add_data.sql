-- Fix missing seller accounts
-- Current Max u_id is 17
-- Sellers with IDs 2 and 3 exist in 'seller' table but not in 'user' table
-- Seller 2: 万科企业股份有限公司 (Vanke)
-- Seller 3: 碧桂园控股有限公司 (Country Garden)

INSERT INTO user (u_id, u_username, u_password, u_type) VALUES 
(18, 'vanke_official', '123456', 'seller'),
(19, 'country_garden', '123456', 'seller');

-- Update seller table to link with new user accounts
UPDATE seller SET s_id = 18 WHERE s_id = 2;
UPDATE seller SET s_id = 19 WHERE s_id = 3;

-- Update house table to reflect new seller IDs
UPDATE house SET h_seller_id = 18 WHERE h_seller_id = 2;
UPDATE house SET h_seller_id = 19 WHERE h_seller_id = 3;

-- Add new test seller
INSERT INTO user (u_id, u_username, u_password, u_type) VALUES
(20, 'poly_real_estate', '123456', 'seller'),
(21, 'china_overseas', '123456', 'seller');

INSERT INTO seller (s_id, s_name, s_phone, s_email, s_website, s_describe) VALUES
(20, '保利发展控股集团', '020-89898888', 'service@poly.com', 'https://www.poly.com.cn', '保利发展控股集团股份有限公司，致力于打造“不动产生态发展平台”，以不动产投资开发为原点，立体延展不动产全产业链。'),
(21, '中国海外发展有限公司', '0755-82826666', 'contact@cohl.com', 'https://www.cohl.com', '中国海外发展有限公司（以下简称“公司”）于1979年在香港注册成立，是中国建筑集团有限公司（“中建集团”）在香港的控股子公司。');

-- Add new test houses for new sellers
-- Note: h_id is auto_increment, so we let DB handle it. If we need to link tags, we need to know IDs. 
-- For simplicity in SQL script without variables, we can assume auto_increment continues from current max (9).
-- But safe way is to just insert houses, and if we want tags, we might need a stored procedure or just simple inserts if we know the order.
-- Let's just insert houses first.

INSERT INTO house (h_seller_id, h_name, h_describe, h_address, h_detail_address, h_price, h_longitude, h_latitude, h_square, h_checked) VALUES
(20, '保利天悦', '广州琶洲核心区，一线江景豪宅。紧邻保利国际广场，商务休闲两不误。', '广州市海珠区', '阅江中路688号', 110000, 113.3667, 23.1000, 200, 1),
(20, '保利和光尘樾', '位于成都市天府新区，低密度洋房社区。现代光影建筑，艺术生活典范。', '成都市天府新区', '梓州大道南段', 25000, 104.0833, 30.4500, 120, 1),
(21, '中海鹿丹名苑', '深圳市罗湖区核心地段，稀缺住宅。周边配套成熟，交通便利，生活氛围浓厚。', '深圳市罗湖区', '滨河大道1001号', 95000, 114.1167, 22.5333, 90, 1),
(21, '中海寰宇天下', '北京市石景山区，科技产业园区核心。全龄宜居社区，高品质精装交付。', '北京市石景山区', '金安桥地铁站旁', 75000, 116.1667, 39.9167, 105, 1);

-- Link tags to new houses (Assuming h_id 10, 11, 12, 13 based on current max 9)
-- House 10 (保利天悦): 江景(1), 豪宅(7), 精装修(4)
INSERT INTO house_tag (ht_house_id, ht_tag_id) VALUES 
(10, 1), (10, 7), (10, 4);

-- House 11 (保利和光尘樾): 公园(10), 精装修(4), 随时看房(5)
INSERT INTO house_tag (ht_house_id, ht_tag_id) VALUES 
(11, 10), (11, 4), (11, 5);

-- House 12 (中海鹿丹名苑): 地铁房(2), 学区房(3), 随时看房(5)
INSERT INTO house_tag (ht_house_id, ht_tag_id) VALUES 
(12, 2), (12, 3), (12, 5);

-- House 13 (中海寰宇天下): 地铁房(2), 精装修(4), 园林(8)
INSERT INTO house_tag (ht_house_id, ht_tag_id) VALUES 
(13, 2), (13, 4), (13, 8);
