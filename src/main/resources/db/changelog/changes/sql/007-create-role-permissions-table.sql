CREATE
    TABLE
        role_permissions(
            role_id BIGINT NOT NULL,
            permission_id BIGINT NOT NULL,
            granted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            granted_by VARCHAR(255),
            PRIMARY KEY(
                role_id,
                permission_id
            ),
            FOREIGN KEY(role_id) REFERENCES roles(id) ON
            DELETE
                CASCADE,
                FOREIGN KEY(permission_id) REFERENCES permissions(id) ON
                DELETE
                    CASCADE
        );

CREATE
    INDEX idx_role_permissions_permission_id ON
    role_permissions(permission_id);
