CREATE
    TABLE
        permissions(
            id BIGSERIAL PRIMARY KEY,
            permission_code VARCHAR(100) NOT NULL UNIQUE,
            resource VARCHAR(100) NOT NULL,
            ACTION VARCHAR(50) NOT NULL,
            description TEXT,
            is_system BOOLEAN DEFAULT FALSE,
            metadata TEXT,
            created_by VARCHAR(255),
            created_on TIMESTAMP,
            last_modified_by VARCHAR(255),
            last_modified_on TIMESTAMP
        );

CREATE
    INDEX idx_permissions_resource ON
    permissions(resource);

CREATE
    INDEX idx_permissions_action ON
    permissions(ACTION);

CREATE
    INDEX idx_permissions_resource_action ON
    permissions(
        resource,
        ACTION
    );
