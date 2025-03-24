CREATE
    TABLE
        users(
            id BIGSERIAL PRIMARY KEY,
            external_id VARCHAR(255) NOT NULL UNIQUE,
            email VARCHAR(255) NOT NULL UNIQUE,
            display_name VARCHAR(255) NOT NULL,
            user_type VARCHAR(20) NOT NULL,
            status VARCHAR(20) NOT NULL,
            created_by VARCHAR(255),
            created_on TIMESTAMP,
            last_modified_by VARCHAR(255),
            last_modified_on TIMESTAMP
        );
