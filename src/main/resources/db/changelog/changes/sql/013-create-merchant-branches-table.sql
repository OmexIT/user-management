CREATE
    TABLE
        merchant_branches(
            id BIGSERIAL PRIMARY KEY,
            merchant_id BIGINT NOT NULL,
            parent_branch_id BIGINT,
            branch_code VARCHAR(50) NOT NULL,
            branch_name VARCHAR(200) NOT NULL,
            branch_type VARCHAR(50) NOT NULL,
            address TEXT,
            phone_number VARCHAR(20),
            email VARCHAR(100),
            manager_name VARCHAR(100),
            is_active BOOLEAN DEFAULT TRUE,
            metadata TEXT,
            created_by VARCHAR(255),
            created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            last_modified_by VARCHAR(255),
            last_modified_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY(merchant_id) REFERENCES merchants(id) ON
            DELETE
                CASCADE,
                FOREIGN KEY(parent_branch_id) REFERENCES merchant_branches(id) ON
                DELETE
                    RESTRICT,
                    UNIQUE(
                        merchant_id,
                        branch_code
                    )
        );

CREATE
    INDEX idx_merchant_branches_merchant_id ON
    merchant_branches(merchant_id);

CREATE
    INDEX idx_merchant_branches_parent_branch_id ON
    merchant_branches(parent_branch_id);

CREATE
    INDEX idx_merchant_branches_branch_code ON
    merchant_branches(branch_code);

CREATE
    INDEX idx_merchant_branches_is_active ON
    merchant_branches(is_active);
