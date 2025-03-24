CREATE
    TABLE
        merchant_users(
            merchant_id BIGINT NOT NULL,
            user_id BIGINT NOT NULL,
            ROLE VARCHAR(50) NOT NULL,
            created_by VARCHAR(255),
            created_on TIMESTAMP,
            last_modified_by VARCHAR(255),
            last_modified_on TIMESTAMP,
            PRIMARY KEY(
                merchant_id,
                user_id
            ),
            CONSTRAINT fk_merchant_user_merchant FOREIGN KEY(merchant_id) REFERENCES merchants(id),
            CONSTRAINT fk_merchant_user_user FOREIGN KEY(user_id) REFERENCES users(id)
        );
