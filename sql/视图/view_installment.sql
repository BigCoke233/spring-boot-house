CREATE OR REPLACE VIEW view_installment AS
SELECT 
    i.i_contract_id AS `vi_contract_id`,
    i.i_down_payment AS vi_down_payment,
    i.i_total_periods AS vi_total_periods,
    i.i_paid_per_period AS vi_paid_per_period,
    i.i_paid_count AS vi_paid_count,
    -- 合同首付比例
    ROUND(i_down_payment / (h.h_price * h.h_square), 2) AS vi_rate_down_payment,
    -- 合同总价
		ROUND(h.h_price * h.h_square, 2) AS vi_total_price,
		-- 合同总尾款
		ROUND(h.h_price * h.h_square - i.i_down_payment, 2) AS vi_final_payment,
		-- 已还尾款
		ROUND(h.h_price * h.h_square - i.i_down_payment - (i.i_paid_count * i.i_paid_per_period), 2) AS vi_paid_final,
		-- 还款状态
	CASE
        WHEN i.i_paid_count = i.i_total_periods THEN 1
        ELSE 0
    END AS `vi_payment_status`
FROM installment i
INNER JOIN contract c ON i.i_contract_id = c.c_id
INNER JOIN house h ON c.c_house_id = h.h_id;