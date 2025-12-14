CREATE OR REPLACE VIEW view_contract AS
SELECT 
    c.c_id AS vc_contract_id,
    c.c_buyer_id AS vc_buyer_id,
    c.c_house_id AS vc_house_id,
    h.h_seller_id AS vc_seller_id,
    c.c_total_price AS vc_total_price,
    c.c_pay_way AS vc_pay_way,
    
    -- 同意状态（卖方同意 && 买方同意）
    CASE 
        WHEN c.c_buyer_agree = 1 AND c.c_seller_agree = 1 THEN 1
        ELSE 0
    END AS vc_agree_status,
    
    -- 付款状态
    CASE 
        -- 已付款（（买方全款 && 买方付款） || （买方分期 &&  买方付完尾款））
        WHEN (c.c_pay_way = 'full' AND c.c_paid = 1) 
             OR (c.c_pay_way = 'installment' AND i.i_paid_count >= i.i_total_periods) 
             THEN 2
        
        -- 部分付款（买方分期 &&  买方付完首付 && 买方未付完尾款）
        WHEN c.c_pay_way = 'installment' 
             AND i.i_paid_count > 0 
             AND i.i_paid_count < i.i_total_periods 
             THEN 1
        
        -- 未付款（（买方全款 && 买方未付款）|| （买方分期 && 买方未付尾款））
        WHEN (c.c_pay_way = 'full' AND (c.c_paid = 0 OR c.c_paid IS NULL))
             OR (c.c_pay_way = 'installment' AND (i.i_paid_count = 0 OR i.i_paid_count IS NULL))
             THEN 0
        
        ELSE -1
    END AS vc_payment_status,
    
    -- 完成状态（已付款 && 已交房）
    CASE 
        WHEN (
            (c.c_pay_way = 'full' AND c.c_paid = 1) 
            OR (c.c_pay_way = 'installment' AND i.i_paid_count >= i.i_total_periods)
        ) 
        AND c.c_delivered = 1 
        THEN 1  
        ELSE 0
    END AS vc_completion_status,
    
    -- 其他相关字段
    c.c_buyer_agree AS vc_buyer_agree,
    c.c_seller_agree AS vc_seller_agree,
    c.c_paid AS vc_paid,
    c.c_delivered AS vc_delivered,
    c.c_paytime_ending AS vc_paytime_ending,
    c.c_paytime_actually AS vc_paytime_actually,
    c.c_delivery_ending AS vc_delivery_ending,
    c.c_delivery_actually AS vc_delivery_actually
    
FROM contract c
LEFT JOIN house h ON c.c_house_id = h.h_id
LEFT JOIN installment i ON c.c_id = i.i_contract_id
WHERE h.h_seller_id IS NOT NULL;