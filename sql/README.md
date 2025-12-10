# 数据库文档README

# 基础表

## 用户基础表-user

表名：user

表描述：记录用户和管理员的登录注册信息

|字段|含义|类型|长度|是否为空|缺省值|
| ------------| ------------------| ---------| ------| ----------| ----------------|
|u_id|用户id（PK自增）|int||no|AUTO_INCREMENT|
|u_type|用户类型|varchar|20|no|无|
|u_username|用户名|varchar|30|no|无|
|u_password|用户密码|varchar|30|no|无|

## 卖家基础表-seller

表名：seller

表描述：记录卖家详细信息

|字段|含义|类型|长度|是否为空|缺省值|
| ------------| ------------------------| ---------| ------| ----------| --------|
|s_id|卖方ID（PK自增）（FK）|int|-|NOT NULL|无|
|s_name|公司名|varchar|255|NOT NULL|无|
|s_describe|公司描述|text|-|NULL|NULL|
|s_phone|公司电话|varchar|20|NULL|NULL|
|s_email|公司邮箱|varchar|50|NULL|NULL|
|s_website|公司官网|varchar|255|NULL|NULL|

### 约束与索引

- ​**主键**​: `s_id`
- ​**外键**​: `s_id`​ → `user.u_id`（ON DELETE CASCADE, ON UPDATE RESTRICT）

## 买家基础表-buyer

表名：buyer

表描述：记录买家详细信息

|字段|含义|类型|长度|是否为空|缺省值|
| -----------------| ------------------------| ---------| ------| ----------| --------|
|b_id|买方ID（PK自增）（FK）|int|-|NOT NULL|无|
|b_name|姓名|varchar|20|NOT NULL|无|
|b_phone|电话|varchar|20|NULL|NULL|
|b_email|邮箱|varchar|50|NULL|NULL|
|b_mobile_assets|移动资产数额|double|-|NULL|NULL|
|b_fixed_assets|固定资产数额|double|-|NULL|NULL|
|b_annual_income|年收入|double|-|NULL|NULL|

### 约束与索引

- **主键**: `b_id`
- **外键**: `b_id`​ → `user.u_id`（ON DELETE CASCADE, ON UPDATE RESTRICT）

## 房源基础表-house

表名：house

表描述：用于记录房源信息

|字段|含义|类型|长度|是否为空|缺省值|
| ------------------| --------------------------| ---------| ------| ----------| ----------------|
|h_id|房源id（PK自增）|int|-|no|AUTO_INCREMENT|
|h_seller_id|卖方id（FK）|int|-|no|无|
|h_name|房源名字|varchar|255|no|无|
|h_describe|房源详情|text|-|yes|NULL|
|h_address|房源地址|varchar|255|yes|NULL|
|h_detail_address|房源门牌号|varchar|255|yes|NULL|
|h_price|房源每平方单价|double|-|yes|NULL|
|h_longitude|房源经度|double|-|yes|0|
|h_latitude|房源纬度|double|-|yes|0|
|h_square|建筑面积（单位：平方米）|double|-|yes|0|
|h_checked|审核状态|int|-|yes|0|

### 约束和索引

- ​**主键**​: `h_id`（降序索引）
- ​**无符号约束**​: `h_square`（确保不为负数）

## 合同基础表-contrast

表名：contrast

表描述：记录合同详细信息和状态

|字段|含义|类型|长度|是否为空|缺省值|
| ---------------------| -------------------------------------------| ----------| ------| ----------| ----------------|
|c_id|合同ID（PK自增）|int|-|NOT NULL|AUTO_INCREMENT|
|c_buyer_id|买方id（FK）|int|-|NOT NULL|无|
|c_house_id|房源id（FK）|int|-|NOT NULL|无|
|c_total_price|房源出售的总价格|double|-|NULL|NULL|
|c_pay_way|付款方式（full：全款，installment：分期）|varchar|20|NULL|NULL|
|c_paytime_ending|买方付款截止日期|datetime|-|NULL|NULL|
|c_paytime_actually|买方付款实际日期|datetime|-|NULL|NULL|
|c_delivery_ending|卖方交房截止日期|datetime|-|NULL|NULL|
|c_delivery_actually|卖方交房实际日期|datetime|-|NULL|NULL|
|c_buyer_agree|买方同意（-1:拒绝,0:未处理,1同意）|int|-|NULL|0|
|c_seller_agree|卖方同意（-1:拒绝,0:未处理,1同意）|int|-|NULL|0|
|c_paid|买方是否付款|int|-|NULL|NULL|
|c_delivered|卖方是否交房|int|-|NULL|NULL|

### 约束和索引

- ​**主键**​: `c_id`（降序索引）
- ​**外键**:

  - ​`c_buyer_id`​ → `buyer.b_id`（RESTRICT级联）
  - ​`c_house_id`​ → `house.h_id`（RESTRICT级联）
- ​**索引**:

  - ​`c_buyer_id`（升序）
  - ​`c_house_id`（升序）
  - ​`c_total_price`（升序）
- ​**检查约束**:

  - ​`c_pay_way` 必须是 'full' 或 'installment'
  - ​`c_paytime_actually`​ > `c_paytime_ending`
  - ​`c_delivery_actually`​ > `c_delivery_ending`
  - ​`c_buyer_agree`​ 和 `c_seller_agree` 必须是 -1, 0, 或 1

## 分期表-installment

表名：installment

表描述：记录分期付款状况

|字段|含义|类型|长度|是否为空|缺省值|
| -------------------| --------------------| --------| ------| ----------| --------|
|i_contrast_id|合同id（PK）（FK）|int|-|NOT NULL|无|
|i_down_payment|首付|double|-|NOT NULL|无|
|i_total_periods|总期数|int|-|NULL|NULL|
|i_paid_per_period|每期还款额|double|-|NULL|NULL|
|i_paid_count|已还款期数|int|-|NULL|NULL|

### 约束和索引

- ​**主键**​: `i_contrast_id`
- ​**外键**​: `i_contrast_id`​ → `contrast.c_id`（RESTRICT级联）
- ​**检查约束**​: `i_paid_count`​ ≤ `i_total_periods`

## **房源标签基础表-tag**

表名：tag

表描述：记录表的标签

|字段|含义|类型|长度|是否为空|缺省值|
| --------| ------------------| ---------| ------| ----------| ----------------|
|t_id|标签ID（PK自增）|int|-|NOT NULL|AUTO_INCREMENT|
|t_name|标签名|varchar|255|NULL|NULL|

## **房源标签关联表-house_tag**

|字段|含义|类型|长度|是否为空|缺省值|
| -------------| --------------------| ------| ------| ----------| --------|
|ht_house_id|房源id（PK）（FK）|int|-|NOT NULL|无|
|ht_tag_id|标签id（PK）（FK）|int|-|NOT NULL|无|

### 约束和索引

- **主键**: `ht_house_id`​，`ht_tag_id`
- **外键**:

  -  `ht_house_id`​ → `house.h_id`（RESTRICT级联）
  -  `ht_tag_id`​ → `tag.t_id`（RESTRICT级联）

## **房源图片表-house_picture**

|字段|含义|类型|长度|是否为空|缺省值|
| -------------------| --------------------| ---------| ------| ----------| --------|
|hp_house_id|房源id（PK）（FK）|int|-|NOT NULL|无|
|hp_picture_number|房源图片编号|int|-|NOT NULL|无|
|hp_picture_path|房源图片路径|varchar|255|NULL|NULL|

- **主键**: `hp_house_id`
- **外键**: `hp_house_id`​ → `house.h_id`（RESTRICT级联）

## **收藏表-follow**

|字段|含义|类型|长度|是否为空|缺省值|
| ------------| ------------------------| ------| ------| ----------| --------|
|f_buyer_id|收藏者id（PK）（FK）|int|-|NOT NULL|无|
|f_house_id|收藏房源id（PK）（FK）|int|-|NOT NULL|无|

### 约束和索引

- **主键**: `f_buyer_id`​，`f_house_id`
- **外键**:

  -  `f_buyer_id`​ → `buy.b_id`（RESTRICT级联）
  -  `f_house_id`​ → `house.h_id`（RESTRICT级联）

‍

# 视图

## **合同综合视图-view_contract**

|字段|含义|类型|长度|是否为空|缺省值|
| ----------------------| -------------------------------| ----------| ------| ----------| --------|
|vc_contract_id|合同ID|int|-|NOT NULL|无|
|vc_buyer_id|买方ID|int|-|NOT NULL|无|
|vc_house_id|房源ID|int|-|NOT NULL|无|
|vc_seller_id|卖方ID|int|-|NOT NULL|无|
|vc_total_price|合同总价|double|-|NULL|NULL|
|vc_pay_way|付款方式|varchar|20|NULL|NULL|
|vc_agree_status|同意状态（双方同意=1）|int|-|NOT NULL|0或1|
|vc_payment_status|付款状态（0未付/1部分/2已付）|int|-|NOT NULL|-1~2|
|vc_completion_status|完成状态（已付款且已交房=1）|int|-|NOT NULL|0或1|
|vc_buyer_agree|买方同意状态|int|-|NULL|NULL|
|vc_seller_agree|卖方同意状态|int|-|NULL|NULL|
|vc_paid|买方是否付款|int|-|NULL|NULL|
|vc_delivered|卖方是否交房|int|-|NULL|NULL|
|vc_paytime_ending|付款截止日期|datetime|-|NULL|NULL|
|vc_paytime_actually|实际付款日期|datetime|-|NULL|NULL|
|vc_delivery_ending|交房截止日期|datetime|-|NULL|NULL|
|vc_delivery_actually|实际交房日期|datetime|-|NULL|NULL|

## **房源综合视图-view_house**

|字段|含义|类型|长度|是否为空|缺省值|
| -------------------| ---------------------------------| ---------------| ------| ----------| --------|
|vh_id|房源ID|int|-|NOT NULL|无|
|vh_seller_id|卖方ID|int|-|NOT NULL|无|
|vh_name|房源名称|varchar|255|NOT NULL|无|
|vh_describe|房源描述|text|-|NULL|NULL|
|vh_address|房源地址|varchar|255|NULL|NULL|
|vh_detail_address|详细地址|varchar|255|NULL|NULL|
|vh_price|每平米单价|double|-|NULL|NULL|
|vh_longitude|经度|double|-|NULL|NULL|
|vh_latitud|纬度|double|-|NULL|NULL|
|vh_square|建筑面积|double|-|NULL|0|
|vh_checked|审核状态|int|-|NULL|0|
|vh_total_price|房源总价（单价×面积）|decimal(10,2)|-|NULL|NULL|
|house_status|房源状态（0已售/1在售/2合同中）|int|-|NOT NULL|1|

## **分期详情视图-view_installment**

|字段|含义|类型|长度|是否为空|缺省值|
| --------------------| -----------------------------| ---------------| ------| ----------| --------|
|vi_contrast_id|合同ID|int|-|NOT NULL|无|
|vi_down_payment|首付款|double|-|NOT NULL|无|
|vi_total_periods|总期数|int|-|NULL|NULL|
|vi_paid_per_period|每期还款额|double|-|NULL|NULL|
|i_paid_count|已还款期数|int|-|NULL|NULL|
|vi_total_price|合同总价|decimal(10,2)|-|NOT NULL|无|
|vi_final_payment|尾款总额（总价-首付）|decimal(10,2)|-|NOT NULL|无|
|vi_paid_final|已还尾款金额|decimal(10,2)|-|NULL|NULL|
|vi_payment_status|还款状态（0未还完/1已还完）|int|-|NOT NULL|0或1|
