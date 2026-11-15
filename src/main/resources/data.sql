INSERT INTO ingredient (name, price, category, supplier, quantity, order_date_time)
SELECT '番茄', 20.00, '蔬菜', '農產合作社', 5, '2026-11-08 14:00:00'
WHERE NOT EXISTS (SELECT 1 FROM ingredient WHERE name = '番茄');

INSERT INTO ingredient (name, price, category, supplier, quantity, order_date_time)
SELECT '雞蛋', 30.50, '蛋類', '養殖場', 12, '2026-11-08 14:30:00'
WHERE NOT EXISTS (SELECT 1 FROM ingredient WHERE name = '雞蛋');

UPDATE ingredient
SET quantity = 0
WHERE quantity IS NULL;

UPDATE ingredient
SET order_date_time = CURRENT_TIMESTAMP
WHERE order_date_time IS NULL;

UPDATE ingredient
SET quantity = 5,
    order_date_time = '2026-11-08 14:00:00'
WHERE name = '番茄' AND quantity = 0;

UPDATE ingredient
SET quantity = 12,
    order_date_time = '2026-11-08 14:30:00'
WHERE name = '雞蛋' AND quantity = 0;

-- 台灣火鍋店常用食材與供應商資料（可於供應商資料頁查閱）
INSERT INTO ingredient (name, price, category, supplier, quantity, order_date_time)
SELECT '台灣梅花豬肉片', 285.00, '肉類', '台畜食品股份有限公司', 18, CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM ingredient WHERE name = '台灣梅花豬肉片');
INSERT INTO ingredient (name, price, category, supplier, quantity, order_date_time)
SELECT '美國牛五花', 420.00, '肉類', '卜蜂企業股份有限公司', 14, CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM ingredient WHERE name = '美國牛五花');
INSERT INTO ingredient (name, price, category, supplier, quantity, order_date_time)
SELECT '去骨雞腿肉', 190.00, '肉類', '大成長城企業股份有限公司', 16, CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM ingredient WHERE name = '去骨雞腿肉');
INSERT INTO ingredient (name, price, category, supplier, quantity, order_date_time)
SELECT '白蝦', 360.00, '海鮮', '元家企業股份有限公司', 9, CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM ingredient WHERE name = '白蝦');
INSERT INTO ingredient (name, price, category, supplier, quantity, order_date_time)
SELECT '鯛魚片', 220.00, '海鮮', '元家企業股份有限公司', 11, CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM ingredient WHERE name = '鯛魚片');
INSERT INTO ingredient (name, price, category, supplier, quantity, order_date_time)
SELECT '高麗菜', 55.00, '蔬菜', '台北農產運銷股份有限公司', 20, CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM ingredient WHERE name = '高麗菜');
INSERT INTO ingredient (name, price, category, supplier, quantity, order_date_time)
SELECT '大陸妹', 65.00, '蔬菜', '台北農產運銷股份有限公司', 15, CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM ingredient WHERE name = '大陸妹');
INSERT INTO ingredient (name, price, category, supplier, quantity, order_date_time)
SELECT '金針菇', 48.00, '菇類', '台北農產運銷股份有限公司', 22, CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM ingredient WHERE name = '金針菇');
INSERT INTO ingredient (name, price, category, supplier, quantity, order_date_time)
SELECT '綜合火鍋料', 135.00, '火鍋料', '桂冠實業股份有限公司', 13, CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM ingredient WHERE name = '綜合火鍋料');
INSERT INTO ingredient (name, price, category, supplier, quantity, order_date_time)
SELECT '凍豆腐', 78.00, '火鍋料', '義美食品股份有限公司', 10, CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM ingredient WHERE name = '凍豆腐');
INSERT INTO ingredient (name, price, category, supplier, quantity, order_date_time)
SELECT '王子麵', 85.00, '麵食', '味丹企業股份有限公司', 25, CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM ingredient WHERE name = '王子麵');
INSERT INTO ingredient (name, price, category, supplier, quantity, order_date_time)
SELECT '蒸煮麵', 85.00, '麵食', '味丹企業股份有限公司', 25, CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM ingredient WHERE name = '蒸煮麵');

-- 叫貨規則：休息日下單與 15:00 後下單，皆以隔日 15:00 前的訂單處理；
-- 配送日若遇休息日，順延至下一個非休息日。
INSERT INTO supplier (name, rest_days, order_cutoff_time)
SELECT '農產合作社', 'MONDAY', '15:00' WHERE NOT EXISTS (SELECT 1 FROM supplier WHERE name = '農產合作社');
INSERT INTO supplier (name, rest_days, order_cutoff_time)
SELECT '養殖場', 'TUESDAY,SUNDAY', '15:00' WHERE NOT EXISTS (SELECT 1 FROM supplier WHERE name = '養殖場');
INSERT INTO supplier (name, rest_days, order_cutoff_time)
SELECT '台畜食品股份有限公司', 'TUESDAY,SATURDAY', '15:00' WHERE NOT EXISTS (SELECT 1 FROM supplier WHERE name = '台畜食品股份有限公司');
INSERT INTO supplier (name, rest_days, order_cutoff_time)
SELECT '卜蜂企業股份有限公司', 'SATURDAY,SUNDAY,MONDAY', '15:00' WHERE NOT EXISTS (SELECT 1 FROM supplier WHERE name = '卜蜂企業股份有限公司');
INSERT INTO supplier (name, rest_days, order_cutoff_time)
SELECT '大成長城企業股份有限公司', 'FRIDAY,SATURDAY', '15:00' WHERE NOT EXISTS (SELECT 1 FROM supplier WHERE name = '大成長城企業股份有限公司');
INSERT INTO supplier (name, rest_days, order_cutoff_time)
SELECT '元家企業股份有限公司', 'WEDNESDAY', '15:00' WHERE NOT EXISTS (SELECT 1 FROM supplier WHERE name = '元家企業股份有限公司');
INSERT INTO supplier (name, rest_days, order_cutoff_time)
SELECT '台北農產運銷股份有限公司', 'MONDAY', '15:00' WHERE NOT EXISTS (SELECT 1 FROM supplier WHERE name = '台北農產運銷股份有限公司');
INSERT INTO supplier (name, rest_days, order_cutoff_time)
SELECT '桂冠實業股份有限公司', 'TUESDAY', '15:00' WHERE NOT EXISTS (SELECT 1 FROM supplier WHERE name = '桂冠實業股份有限公司');
INSERT INTO supplier (name, rest_days, order_cutoff_time)
SELECT '義美食品股份有限公司', 'TUESDAY', '15:00' WHERE NOT EXISTS (SELECT 1 FROM supplier WHERE name = '義美食品股份有限公司');
INSERT INTO supplier (name, rest_days, order_cutoff_time)
SELECT '味丹企業股份有限公司', 'SUNDAY', '15:00' WHERE NOT EXISTS (SELECT 1 FROM supplier WHERE name = '味丹企業股份有限公司');
