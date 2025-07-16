CREATE
    TABLE
        portal_users(
            id BIGSERIAL PRIMARY KEY,
            user_id BIGINT NOT NULL UNIQUE,
            employee_id VARCHAR(100) UNIQUE,
            department VARCHAR(200),
            access_level VARCHAR(50),
            can_impersonate BOOLEAN DEFAULT FALSE,
            last_impersonation_at TIMESTAMP,
            metadata TEXT,
            created_by VARCHAR(255),
            created_on TIMESTAMP,
            last_modified_by VARCHAR(255),
            last_modified_on TIMESTAMP,
            FOREIGN KEY(user_id) REFERENCES users(id) ON
            DELETE
                CASCADE
        );

CREATE
    INDEX idx_portal_users_employee_id ON
    portal_users(employee_id);

CREATE
    INDEX idx_portal_users_department ON
    portal_users(department);

CREATE
    INDEX idx_portal_users_access_level ON
    portal_users(access_level);
