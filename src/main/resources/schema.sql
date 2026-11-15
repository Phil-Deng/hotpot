CREATE TABLE IF NOT EXISTS ingredient (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    category VARCHAR(100) NOT NULL,
    supplier VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    order_date_time TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS supplier (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    rest_days VARCHAR(100) NOT NULL,
    order_cutoff_time VARCHAR(5) NOT NULL,
    PRIMARY KEY (id)
);

