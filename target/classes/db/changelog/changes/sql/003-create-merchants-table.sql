CREATE
    TABLE
        merchants(
            id BIGSERIAL PRIMARY KEY,
            merchant_code VARCHAR(50) NOT NULL UNIQUE,
            business_name VARCHAR(255) NOT NULL,
            status VARCHAR(50) NOT NULL,
            created_by VARCHAR(255),
            created_on TIMESTAMP,
            last_modified_by VARCHAR(255),
            last_modified_on TIMESTAMP
        );
