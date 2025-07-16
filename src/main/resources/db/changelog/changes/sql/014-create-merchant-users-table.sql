CREATE
    TABLE
        merchant_users(
            id BIGSERIAL PRIMARY KEY,
            merchant_id BIGINT NOT NULL,
            user_id BIGINT NOT NULL,
            branch_id BIGINT,
            role_id BIGINT,
            is_primary_merchant BOOLEAN DEFAULT FALSE,
            permissions_scope VARCHAR(50) DEFAULT 'MERCHANT_WIDE',
            custom_attributes TEXT,
            is_active BOOLEAN DEFAULT TRUE,
            created_by VARCHAR(255),
            created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            last_modified_by VARCHAR(255),
            last_modified_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            CONSTRAINT fk_merchant_user_merchant FOREIGN KEY(merchant_id) REFERENCES merchants(id) ON
            DELETE
                CASCADE,
                CONSTRAINT fk_merchant_user_user FOREIGN KEY(user_id) REFERENCES users(id) ON
                DELETE
                    CASCADE,
                    CONSTRAINT fk_merchant_user_branch FOREIGN KEY(branch_id) REFERENCES merchant_branches(id) ON
                    DELETE
                    SET
                        NULL,
                        CONSTRAINT fk_merchant_user_role FOREIGN KEY(role_id) REFERENCES roles(id) ON
                        DELETE
                        SET
                            NULL,
                            UNIQUE(
                                user_id,
                                merchant_id,
                                branch_id
                            )
        );

CREATE
    INDEX idx_merchant_users_merchant_id ON
    merchant_users(merchant_id);

CREATE
    INDEX idx_merchant_users_user_id ON
    merchant_users(user_id);

CREATE
    INDEX idx_merchant_users_branch_id ON
    merchant_users(branch_id);

CREATE
    INDEX idx_merchant_users_role_id ON
    merchant_users(role_id);

CREATE
    INDEX idx_merchant_users_is_active ON
    merchant_users(is_active);

CREATE
    INDEX idx_merchant_users_permissions_scope ON
    merchant_users(permissions_scope);
