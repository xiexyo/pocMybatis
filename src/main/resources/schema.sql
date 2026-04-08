CREATE TABLE IF NOT EXISTS t_customer (
    customer_id      VARCHAR(32) PRIMARY KEY,
    customer_name    VARCHAR(64) NOT NULL,
    cert_type        VARCHAR(16) NOT NULL,
    cert_no          VARCHAR(32) NOT NULL,
    mobile           VARCHAR(20) NOT NULL,
    customer_status  VARCHAR(16) NOT NULL,
    risk_level       VARCHAR(16),
    created_time     TIMESTAMP NOT NULL,
    updated_time     TIMESTAMP NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_customer_cert_no
    ON t_customer(cert_no);

CREATE TABLE IF NOT EXISTS t_product (
    product_code     VARCHAR(32) PRIMARY KEY,
    product_name     VARCHAR(128) NOT NULL,
    product_type     VARCHAR(32) NOT NULL,
    currency         VARCHAR(8) NOT NULL,
    sale_status      VARCHAR(16) NOT NULL,
    account_level    VARCHAR(16) NOT NULL,
    created_time     TIMESTAMP NOT NULL,
    updated_time     TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS t_account (
    account_no       VARCHAR(32) PRIMARY KEY,
    customer_id      VARCHAR(32) NOT NULL,
    account_name     VARCHAR(64) NOT NULL,
    account_type     VARCHAR(32) NOT NULL,
    product_code     VARCHAR(32) NOT NULL,
    currency         VARCHAR(8) NOT NULL,
    balance          DECIMAL(18,2) NOT NULL,
    account_status   VARCHAR(16) NOT NULL,
    branch_code      VARCHAR(16),
    open_date        TIMESTAMP NOT NULL,
    created_time     TIMESTAMP NOT NULL,
    updated_time     TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_account_customer_id
    ON t_account(customer_id);

CREATE TABLE IF NOT EXISTS t_transaction (
    txn_id            VARCHAR(32) PRIMARY KEY,
    request_id        VARCHAR(64) NOT NULL,
    debit_account_no  VARCHAR(32),
    credit_account_no VARCHAR(32),
    txn_type          VARCHAR(32) NOT NULL,
    txn_status        VARCHAR(16) NOT NULL,
    amount            DECIMAL(18,2) NOT NULL,
    remark            VARCHAR(255),
    txn_time          TIMESTAMP NOT NULL,
    created_time      TIMESTAMP NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_transaction_request_id
    ON t_transaction(request_id);

CREATE TABLE IF NOT EXISTS t_idempotent_record (
    request_id        VARCHAR(64) PRIMARY KEY,
    business_type     VARCHAR(32) NOT NULL,
    business_key      VARCHAR(64),
    process_status    VARCHAR(16) NOT NULL,
    response_code     VARCHAR(16),
    response_message  VARCHAR(255),
    created_time      TIMESTAMP NOT NULL,
    updated_time      TIMESTAMP NOT NULL
);