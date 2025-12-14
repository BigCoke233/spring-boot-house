CREATE OR REPLACE VIEW view_house AS
SELECT 
    h.h_id AS vh_id,
    h.h_seller_id AS vh_seller_id,
    h.h_name AS vh_name,
    h.h_describe AS vh_describe,
    h.h_address AS vh_address,
    h.h_detail_address AS vh_detail_address,
    h.h_price AS vh_price,
    h.h_longitude AS vh_longitude,
    h.h_latitude AS vh_latitud,
    h.h_square AS vh_square,
    h.h_checked AS vh_checked,
    
    -- 总价（单价*建筑面积）
    ROUND(h.h_price * h.h_square, 2) AS vh_total_price,
    
    -- 房源状态
    CASE 
        -- 已售（合同已完成）：有已完成合同
        WHEN EXISTS (
            SELECT 1 
            FROM contract c
            JOIN house h2 ON c.c_house_id = h2.h_id
            WHERE h2.h_id = h.h_id
            AND (
                -- 全款且已付款且已交房
                (c.c_pay_way = 'full' AND c.c_paid = 1 AND c.c_delivered = 1)
                OR 
                -- 分期且已付完全款且已交房
                (c.c_pay_way = 'installment' 
                 AND EXISTS (
                     SELECT 1 
                     FROM installment i 
                     WHERE i.i_contract_id = c.c_id
                     AND i.i_paid_count >= i.i_total_periods
                 )
                 AND c.c_delivered = 1)
            )
        ) THEN 0
        
        -- 合同中（合同未完成）：有未完成合同
        WHEN EXISTS (
            SELECT 1 
            FROM contract c
            JOIN house h2 ON c.c_house_id = h2.h_id
            WHERE h2.h_id = h.h_id
            AND (
                -- 合同双方都同意
                (c.c_buyer_agree = 1 AND c.c_seller_agree = 1)
                AND (
                    -- 全款未完成付款或交房
                    (c.c_pay_way = 'full' AND (c.c_paid = 0 OR c.c_delivered = 0))
                    OR 
                    -- 分期未完成付款或交房
                    (c.c_pay_way = 'installment' 
                     AND EXISTS (
                         SELECT 1 
                         FROM installment i 
                         WHERE i.i_contract_id = c.c_id
                         AND (i.i_paid_count < i.i_total_periods OR c.c_delivered = 0)
                     ))
                )
            )
        ) THEN 2
        
        -- 在售（无对应合同或合同未达成）：没有合同或合同未达成协议
        ELSE 1
    END AS house_status

FROM house h;