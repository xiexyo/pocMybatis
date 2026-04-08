MERGE INTO t_product (
    product_code, product_name, product_type, currency, sale_status, account_level, created_time, updated_time
) KEY(product_code)
VALUES (
    'SAV001', '个人人民币一类活期', 'CURRENT', 'CNY', 'ON_SALE', 'CLASS_I', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

MERGE INTO t_product (
    product_code, product_name, product_type, currency, sale_status, account_level, created_time, updated_time
) KEY(product_code)
VALUES (
    'SAV002', '个人人民币二类活期', 'CURRENT', 'CNY', 'ON_SALE', 'CLASS_II', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

MERGE INTO t_product (
    product_code, product_name, product_type, currency, sale_status, account_level, created_time, updated_time
) KEY(product_code)
VALUES (
    'SAV003', '个人美元活期-停售示例', 'CURRENT', 'USD', 'OFF_SALE', 'CLASS_II', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);