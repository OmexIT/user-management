CREATE
    TABLE
        customers(
            id BIGSERIAL PRIMARY KEY,
            customer_number VARCHAR(50) NOT NULL UNIQUE,
            name VARCHAR(255) NOT NULL,
            TYPE VARCHAR(50) NOT NULL,
            user_id BIGINT UNIQUE,
            created_by VARCHAR(255),
            created_on TIMESTAMP,
            last_modified_by VARCHAR(255),
            last_modified_on TIMESTAMP,
            CONSTRAINT fk_customer_user FOREIGN KEY(user_id) REFERENCES users(id)
        );
