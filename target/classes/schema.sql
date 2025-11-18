CREATE TABLE IF NOT EXISTS stock_symbol (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    symbol VARCHAR(50) NOT NULL,
    company_name VARCHAR(500),
    series VARCHAR(10),
    date_of_listing VARCHAR(50),
    paid_up_value BIGINT,
    market_lot BIGINT,
    isin VARCHAR(50),
    face_value BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_symbol UNIQUE (symbol)
);
