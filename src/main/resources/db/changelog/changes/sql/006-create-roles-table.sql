CREATE
    TABLE
        roles(
            id BIGSERIAL PRIMARY KEY,
            role_code VARCHAR(100) NOT NULL UNIQUE,
            role_name VARCHAR(200) NOT NULL,
            description TEXT,
            role_type VARCHAR(50),
            is_system BOOLEAN DEFAULT FALSE,
            metadata TEXT,
            created_by VARCHAR(255),
            created_on TIMESTAMP,
            last_modified_by VARCHAR(255),
            last_modified_on TIMESTAMP
        );

CREATE
    INDEX idx_roles_role_type ON
    roles(role_type);

CREATE
    INDEX idx_roles_is_system ON
    roles(is_system);
